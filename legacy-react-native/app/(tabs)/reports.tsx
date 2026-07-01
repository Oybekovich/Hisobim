import { View, ScrollView, StyleSheet, TouchableOpacity } from 'react-native';
import { Text, ActivityIndicator } from 'react-native-paper';
import { LinearGradient } from 'expo-linear-gradient';
import { Ionicons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { useShopStore } from '../../src/stores/shop.store';
import { useReports } from '../../src/hooks/useReports';
import { formatAmount } from '../../src/utils/format';
import EmptyState from '../../src/components/EmptyState';
import GlassBackground from '../../src/components/GlassBackground';
import GlassCard from '../../src/components/GlassCard';
import HeroCard from '../../src/components/HeroCard';
import { colors, type, radius, spacing, blueGradient } from '../../src/theme';

function getInitials(name: string): string {
  const parts = name.trim().split(' ');
  if (parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
  return name.slice(0, 2).toUpperCase();
}

export default function ReportsScreen() {
  const { activeShop } = useShopStore();
  const { data, isLoading } = useReports(activeShop?.id ?? '');

  if (isLoading) {
    return (
      <GlassBackground style={styles.loading}>
        <ActivityIndicator size="large" color={colors.accentBlue600} />
      </GlassBackground>
    );
  }

  const hasDebtors = (data?.topDebtors?.length ?? 0) > 0;

  return (
    <GlassBackground>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>

        {/* Umumiy qarzdorlik — hero karta */}
        <HeroCard style={styles.hero}>
          <View style={styles.heroInner}>
            <Text style={styles.heroLabel}>UMUMIY QARZDORLIK</Text>
            <Text style={styles.heroAmount}>{formatAmount(data?.totalDebt ?? 0)}</Text>
            <Text style={styles.heroSub}>{data?.debtorCount ?? 0} ta mijoz qarzdor</Text>
          </View>
        </HeroCard>

        {/* Statistika paneli */}
        <GlassCard style={styles.statsCard}>
          <View style={styles.statsRow}>
            <View style={styles.statBox}>
              <Text style={styles.statNumber}>{data?.customerCount ?? 0}</Text>
              <Text style={styles.statLabel}>Jami mijoz</Text>
            </View>
            <View style={styles.statDivider} />
            <View style={styles.statBox}>
              <Text style={[styles.statNumber, { color: colors.debtRed }]}>{data?.debtorCount ?? 0}</Text>
              <Text style={styles.statLabel}>Qarzdor</Text>
            </View>
            <View style={styles.statDivider} />
            <View style={styles.statBox}>
              <Text style={[styles.statNumber, { color: colors.paidNeutral }]}>{data?.clearedCount ?? 0}</Text>
              <Text style={styles.statLabel}>To'lagan</Text>
            </View>
          </View>
        </GlassCard>

        {/* Eng ko'p qarzdorlar */}
        <Text style={styles.sectionTitle}>ENG KO'P QARZDORLAR</Text>
        {hasDebtors ? (
          <GlassCard style={styles.listCard}>
            {data!.topDebtors.map((customer, index) => (
              <TouchableOpacity
                key={customer.id}
                style={[styles.debtorRow, index > 0 && styles.debtorBorder]}
                onPress={() => router.push(`/customer/${customer.id}`)}
                activeOpacity={0.6}
              >
                <Text style={styles.debtorRank}>{index + 1}</Text>
                <LinearGradient colors={blueGradient} style={styles.debtorAvatar}>
                  <Text style={styles.debtorAvatarText}>{getInitials(customer.name)}</Text>
                </LinearGradient>
                <View style={styles.debtorInfo}>
                  <Text style={styles.debtorName} numberOfLines={1}>{customer.name}</Text>
                  {customer.phone ? (
                    <Text style={styles.debtorPhone}>{customer.phone}</Text>
                  ) : null}
                </View>
                <Text style={styles.debtorAmount}>{formatAmount(customer.total_debt)}</Text>
                <Ionicons name="chevron-forward" size={16} color={colors.slate200} style={{ marginLeft: 4 }} />
              </TouchableOpacity>
            ))}
          </GlassCard>
        ) : (
          <EmptyState
            message="Qarzdor mijozlar yo'q"
            subMessage="Barcha mijozlar hisobini tozalagan"
            icon="checkmark-circle-outline"
          />
        )}

      </ScrollView>
    </GlassBackground>
  );
}

const styles = StyleSheet.create({
  loading: { justifyContent: 'center', alignItems: 'center' },
  content: { padding: spacing.md, paddingBottom: 90 },

  hero: { marginBottom: spacing.m },
  heroInner: { padding: spacing.xxl, alignItems: 'center' },
  heroLabel: { ...type.overline, color: 'rgba(255,255,255,0.75)' },
  heroAmount: { ...type.display, color: colors.white, fontSize: 30, marginTop: spacing.s },
  heroSub: { ...type.caption, color: 'rgba(255,255,255,0.7)', marginTop: spacing.s },

  statsCard: { marginBottom: spacing.m },
  statsRow: { flexDirection: 'row', padding: spacing.lg },
  statBox: { flex: 1, alignItems: 'center', paddingVertical: spacing.xs },
  statDivider: { width: 1, backgroundColor: colors.slate100, opacity: 0.6 },
  statNumber: { ...type.display, fontSize: 24, color: colors.ink900 },
  statLabel: { ...type.caption, marginTop: spacing.xs, textAlign: 'center' },

  sectionTitle: { ...type.overline, marginBottom: spacing.s, marginLeft: spacing.xs },
  listCard: {},
  debtorRow: { flexDirection: 'row', alignItems: 'center', paddingHorizontal: spacing.md, paddingVertical: spacing.m },
  debtorBorder: { borderTopWidth: 1, borderTopColor: 'rgba(203,209,220,0.4)' },
  debtorRank: { ...type.amount, color: colors.slate100, width: 18, marginRight: spacing.s },
  debtorAvatar: { width: 36, height: 36, borderRadius: radius.round, justifyContent: 'center', alignItems: 'center', marginRight: spacing.s },
  debtorAvatarText: { ...type.label, color: colors.white },
  debtorInfo: { flex: 1, minWidth: 0 },
  debtorName: { ...type.body },
  debtorPhone: { ...type.caption, marginTop: 1 },
  debtorAmount: { ...type.amount, color: colors.debtRed },
});
