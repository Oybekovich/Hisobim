import { useState } from 'react';
import { View, ScrollView, StyleSheet, TouchableOpacity } from 'react-native';
import { Text, ActivityIndicator } from 'react-native-paper';
import { LinearGradient } from 'expo-linear-gradient';
import { Ionicons } from '@expo/vector-icons';
import { useLocalSearchParams, router, Stack } from 'expo-router';
import { useCustomerById, useDeleteCustomer } from '../../src/hooks/useCustomers';
import { useDebts, useDeleteDebt } from '../../src/hooks/useDebts';
import { useShopStore } from '../../src/stores/shop.store';
import DebtItem from '../../src/components/DebtItem';
import EmptyState from '../../src/components/EmptyState';
import GlassBackground from '../../src/components/GlassBackground';
import HeroCard from '../../src/components/HeroCard';
import ConfirmDialog from '../../src/components/ConfirmDialog';
import { formatAmount } from '../../src/utils/format';
import { colors, type, radius, spacing, blueGradient, heroShadow } from '../../src/theme';

export default function CustomerDetailScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { activeShop } = useShopStore();
  const { data: customer, isLoading: customerLoading } = useCustomerById(id, activeShop?.id);
  const { data: debts = [], isLoading: debtsLoading } = useDebts(id);
  const deleteDebt = useDeleteDebt(id);
  const deleteCustomer = useDeleteCustomer(activeShop?.id ?? '');

  const [debtToDelete, setDebtToDelete] = useState<string | null>(null);
  const [confirmDeleteCustomer, setConfirmDeleteCustomer] = useState(false);

  const initials = customer
    ? customer.name.trim().split(' ').slice(0, 2).map(w => w[0]).join('').toUpperCase()
    : '';

  const handleDeleteDebt = (debtId: string) => setDebtToDelete(debtId);
  const handleDeleteCustomer = () => setConfirmDeleteCustomer(true);

  const confirmDeleteDebt = () => {
    if (debtToDelete) deleteDebt.mutate(debtToDelete);
    setDebtToDelete(null);
  };

  const confirmDeleteCustomerAction = () => {
    setConfirmDeleteCustomer(false);
    deleteCustomer.mutate(id, { onSuccess: () => router.back() });
  };

  return (
    <>
      <Stack.Screen
        options={{
          headerTitleAlign: 'left',
          headerTitle: () => customer ? (
            <View style={styles.headerTitle}>
              <LinearGradient colors={blueGradient} style={styles.headerAvatar}>
                <Text style={styles.headerAvatarText}>{initials}</Text>
              </LinearGradient>
              <View style={styles.headerTexts}>
                <Text style={styles.headerName} numberOfLines={1}>{customer.name}</Text>
                {customer.phone ? (
                  <Text style={styles.headerPhone} numberOfLines={1}>{customer.phone}</Text>
                ) : null}
              </View>
            </View>
          ) : null,
          headerRight: () => customer ? (
            <View style={styles.headerActions}>
              <TouchableOpacity
                style={styles.headerBtn}
                onPress={() => router.push({ pathname: '/customer/edit', params: { id } })}
                hitSlop={6}
              >
                <Ionicons name="pencil-outline" size={19} color={colors.accentBlue600} />
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.headerBtn}
                onPress={handleDeleteCustomer}
                hitSlop={6}
              >
                <Ionicons name="trash-outline" size={19} color={colors.debtRed} />
              </TouchableOpacity>
            </View>
          ) : null,
        }}
      />

      <GlassBackground>
        {customerLoading ? (
          <View style={styles.loading}>
            <ActivityIndicator size="large" color={colors.accentBlue600} />
          </View>
        ) : !customer ? null : (
          <View style={styles.container}>

            {/* Qarz hero kartasi */}
            <HeroCard style={styles.hero}>
              <View style={styles.heroInner}>
                <Text style={styles.heroLabel}>JAMI QARZ</Text>
                <Text style={styles.heroAmount}>{formatAmount(customer.total_debt)}</Text>
              </View>
            </HeroCard>

            {/* Ikki ustunli daftar */}
            <ScrollView contentContainerStyle={styles.list} showsVerticalScrollIndicator={false}>
              {debts.length === 0 && !debtsLoading ? (
                <EmptyState
                  message="Hali yozuv yo'q"
                  subMessage="Quyidagi tugmani bosing"
                  icon="document-text-outline"
                />
              ) : (
                <View style={styles.columns}>
                  <View style={styles.column}>
                    <Text style={[styles.colHeader, { color: colors.debtRed }]}>QARZ OLDI</Text>
                    {debts.filter(d => d.amount > 0).map(item => (
                      <DebtItem key={item.id} debt={item} onDelete={() => handleDeleteDebt(item.id)} />
                    ))}
                  </View>
                  <View style={styles.dividerVertical} />
                  <View style={styles.column}>
                    <Text style={[styles.colHeader, { color: colors.paidGreen }]}>TO'LOV QILDI</Text>
                    {debts.filter(d => d.amount < 0).map(item => (
                      <DebtItem key={item.id} debt={item} onDelete={() => handleDeleteDebt(item.id)} />
                    ))}
                  </View>
                </View>
              )}
            </ScrollView>

            <TouchableOpacity
              style={styles.addButton}
              activeOpacity={0.88}
              onPress={() => router.push({ pathname: '/debt/add', params: { customerId: id } })}
            >
              <LinearGradient colors={blueGradient} style={styles.addButtonGradient}>
                <Ionicons name="add" size={20} color={colors.white} />
                <Text style={styles.addButtonText}>Yozuv qo'shish</Text>
              </LinearGradient>
            </TouchableOpacity>
          </View>
        )}
      </GlassBackground>

      <ConfirmDialog
        visible={debtToDelete !== null}
        title="Yozuvni o'chirish"
        message="Bu yozuvni o'chirishni tasdiqlaysizmi?"
        confirmLabel="O'chirish"
        onConfirm={confirmDeleteDebt}
        onDismiss={() => setDebtToDelete(null)}
      />

      <ConfirmDialog
        visible={confirmDeleteCustomer}
        title="Mijozni o'chirish"
        message={`${customer?.name ?? ''} va barcha qarzlarini o'chirishni tasdiqlaysizmi?`}
        confirmLabel="O'chirish"
        onConfirm={confirmDeleteCustomerAction}
        onDismiss={() => setConfirmDeleteCustomer(false)}
      />
    </>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  loading: { flex: 1, justifyContent: 'center', alignItems: 'center' },

  // Nav header
  headerTitle: { flexDirection: 'row', alignItems: 'center', gap: spacing.s },
  headerAvatar: { width: 34, height: 34, borderRadius: radius.round, justifyContent: 'center', alignItems: 'center' },
  headerAvatarText: { ...type.label, color: colors.white, fontSize: 13 },
  headerTexts: { justifyContent: 'center', gap: 1 },
  headerName: { ...type.h2, fontSize: 15, lineHeight: 18 },
  headerPhone: { ...type.caption, lineHeight: 15 },
  headerActions: { flexDirection: 'row', gap: spacing.sm, marginRight: spacing.xs },
  headerBtn: {
    width: 36,
    height: 36,
    borderRadius: radius.avatarBox,
    backgroundColor: 'rgba(255,255,255,0.6)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.7)',
    justifyContent: 'center',
    alignItems: 'center',
  },

  // Hero
  hero: { marginHorizontal: spacing.md, marginTop: spacing.xxl, marginBottom: spacing.md },
  heroInner: { paddingVertical: spacing.xl, paddingHorizontal: spacing.xl, alignItems: 'center' },
  heroLabel: { ...type.overline, color: 'rgba(255,255,255,0.75)' },
  heroAmount: { ...type.display, color: colors.white, fontSize: 26, marginTop: spacing.sm },

  // Columns
  list: { padding: spacing.s, paddingBottom: 100 },
  columns: { flexDirection: 'row' },
  column: { flex: 1, minWidth: 0 },
  colHeader: { ...type.overline, marginBottom: spacing.s, paddingHorizontal: 2 },
  dividerVertical: { width: 1, backgroundColor: colors.slate100, opacity: 0.5, marginHorizontal: spacing.s },

  addButton: { margin: spacing.md, borderRadius: radius.card, ...heroShadow },
  addButtonGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: spacing.sm,
    height: 52,
    borderRadius: radius.card,
  },
  addButtonText: { ...type.h2, color: colors.white, fontSize: 15 },
});
