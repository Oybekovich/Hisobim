import React from 'react';
import { View, StyleSheet, TouchableOpacity } from 'react-native';
import { Text } from 'react-native-paper';
import { LinearGradient } from 'expo-linear-gradient';
import { Ionicons } from '@expo/vector-icons';
import type { Customer } from '../types';
import { formatAmount } from '../utils/format';
import GlassCard from './GlassCard';
import { colors, type, radius, spacing, blueGradient } from '../theme';

type Props = {
  customer: Customer;
  onPress: () => void;
};

function getInitials(name: string): string {
  const parts = name.trim().split(' ');
  if (parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
  return name.slice(0, 2).toUpperCase();
}

export default function CustomerCard({ customer, onPress }: Props) {
  const hasDebt = customer.total_debt > 0;
  const initials = getInitials(customer.name);

  return (
    <TouchableOpacity onPress={onPress} activeOpacity={0.7} style={styles.wrap}>
      <GlassCard>
        <View style={styles.row}>
          <LinearGradient colors={blueGradient} style={styles.avatar}>
            <Text style={styles.avatarText}>{initials}</Text>
          </LinearGradient>
          <View style={styles.info}>
            <Text style={styles.name} numberOfLines={1}>{customer.name}</Text>
            {customer.phone ? (
              <Text style={styles.phone} numberOfLines={1}>{customer.phone}</Text>
            ) : null}
          </View>
          <View style={styles.right}>
            <Text style={[styles.debt, { color: hasDebt ? colors.debtRed : colors.slate300 }]}>
              {formatAmount(customer.total_debt)}
            </Text>
            <Ionicons name="chevron-forward" size={16} color={colors.slate200} style={styles.chevron} />
          </View>
        </View>
      </GlassCard>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  wrap: { marginHorizontal: spacing.md, marginVertical: spacing.xs },
  row: { flexDirection: 'row', alignItems: 'center', padding: spacing.m },
  avatar: {
    width: 42,
    height: 42,
    borderRadius: radius.round,
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatarText: { ...type.label, color: colors.white, fontSize: 13 },
  info: { flex: 1, paddingHorizontal: spacing.m, minWidth: 0 },
  name: { ...type.body },
  phone: { ...type.caption, marginTop: 2 },
  right: { flexDirection: 'row', alignItems: 'center', gap: 2 },
  debt: { ...type.amount },
  chevron: { marginTop: 1 },
});
