import { useState, useEffect } from 'react';
import { FlatList, View, StyleSheet, RefreshControl, TouchableOpacity } from 'react-native';
import { Text, ActivityIndicator } from 'react-native-paper';
import { SafeAreaView } from 'react-native-safe-area-context';
import { LinearGradient } from 'expo-linear-gradient';
import { Ionicons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { useAuthStore } from '../../src/stores/auth.store';
import { useShopStore } from '../../src/stores/shop.store';
import { useCustomers } from '../../src/hooks/useCustomers';
import { fetchOrCreateShop } from '../../src/services/shop.service';
import CustomerCard from '../../src/components/CustomerCard';
import SearchBar from '../../src/components/SearchBar';
import EmptyState from '../../src/components/EmptyState';
import GlassBackground from '../../src/components/GlassBackground';
import { formatAmount } from '../../src/utils/format';
import { colors, type, radius, spacing, blueGradient, heroShadow } from '../../src/theme';
import type { Customer } from '../../src/types';

export default function HomeScreen() {
  const [searchQuery, setSearchQuery] = useState('');
  const { user } = useAuthStore();
  const { activeShop, setShop } = useShopStore();
  const { data: customers = [], isLoading, refetch } = useCustomers(activeShop?.id ?? '');

  useEffect(() => {
    if (!user || activeShop) return;
    fetchOrCreateShop(user.id, user.phone ?? null).then(setShop);
  }, [user, activeShop]);

  const filtered: Customer[] = customers.filter(
    (c) =>
      c.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      (c.phone ?? '').includes(searchQuery)
  );

  const totalDebt = customers.reduce((sum, c) => sum + c.total_debt, 0);

  if (!activeShop) {
    return (
      <GlassBackground style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={colors.accentBlue600} />
      </GlassBackground>
    );
  }

  return (
    <GlassBackground>
      <SafeAreaView style={styles.container} edges={['top']}>
        <View style={styles.shopHeader}>
          <View style={styles.shopHeaderText}>
            <Text style={styles.shopName} numberOfLines={1}>{activeShop.name}</Text>
            <Text style={styles.shopMeta}>{customers.length} ta mijoz</Text>
          </View>
          {totalDebt > 0 && (
            <View style={styles.totalBadge}>
              <Text style={styles.totalLabel}>JAMI QARZ</Text>
              <Text style={styles.totalAmount}>{formatAmount(totalDebt)}</Text>
            </View>
          )}
        </View>

        <SearchBar value={searchQuery} onChangeText={setSearchQuery} />

        <FlatList
          data={filtered}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <CustomerCard
              customer={item}
              onPress={() => router.push(`/customer/${item.id}`)}
            />
          )}
          ListEmptyComponent={
            <EmptyState
              message={searchQuery ? 'Hech narsa topilmadi' : "Hali mijoz yo'q"}
              subMessage={searchQuery ? undefined : "Qo'shish uchun + tugmasini bosing"}
              icon={searchQuery ? 'search-outline' : 'people-outline'}
            />
          }
          refreshControl={
            <RefreshControl refreshing={isLoading} onRefresh={refetch} tintColor={colors.accentBlue600} />
          }
          contentContainerStyle={filtered.length === 0 ? styles.emptyList : styles.list}
          showsVerticalScrollIndicator={false}
        />

        <TouchableOpacity
          style={styles.fab}
          activeOpacity={0.85}
          onPress={() => router.push('/customer/add')}
        >
          <LinearGradient colors={blueGradient} style={styles.fabGradient}>
            <Ionicons name="add" size={34} color={colors.white} />
          </LinearGradient>
        </TouchableOpacity>
      </SafeAreaView>
    </GlassBackground>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  loadingContainer: { justifyContent: 'center', alignItems: 'center' },

  shopHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-end',
    paddingHorizontal: spacing.lg,
    paddingTop: spacing.s,
    paddingBottom: spacing.m,
  },
  shopHeaderText: { flex: 1, minWidth: 0, paddingRight: spacing.m },
  shopName: { ...type.h1 },
  shopMeta: { ...type.caption, marginTop: 2 },
  totalBadge: { alignItems: 'flex-end' },
  totalLabel: { ...type.overline, color: colors.slate400 },
  totalAmount: { ...type.amount, color: colors.debtRed, fontSize: 15, marginTop: 2 },

  list: { paddingTop: spacing.xs, paddingBottom: 100 },
  emptyList: { flexGrow: 1 },
  fab: {
    position: 'absolute',
    right: spacing.lg,
    bottom: 84,
    borderRadius: radius.fab,
    ...heroShadow,
  },
  fabGradient: {
    width: 58,
    height: 58,
    borderRadius: radius.fab,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
