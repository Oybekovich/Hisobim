import React from 'react';
import { View, StyleSheet, TouchableOpacity } from 'react-native';
import { Text } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';
import type { Debt } from '../types';
import { formatAmount, formatDate } from '../utils/format';
import GlassCard from './GlassCard';
import { colors, type, radius, spacing } from '../theme';

type Props = {
  debt: Debt;
  onDelete: () => void;
};

export default function DebtItem({ debt, onDelete }: Props) {
  const isPayment = debt.amount < 0;
  const amountColor = isPayment ? colors.ink600 : colors.debtRed;

  return (
    <View style={styles.wrap}>
      <GlassCard borderRadius={radius.avatarBox} shadow={false}>
        <View style={styles.content}>
          <View style={styles.row}>
            <Text style={[styles.amount, { color: amountColor }]} numberOfLines={1} adjustsFontSizeToFit>
              {formatAmount(Math.abs(debt.amount))}
            </Text>
            <TouchableOpacity onPress={onDelete} hitSlop={10}>
              <Ionicons name="close" size={14} color={colors.slate300} />
            </TouchableOpacity>
          </View>
          {debt.description ? (
            <Text style={styles.description} numberOfLines={1}>{debt.description}</Text>
          ) : null}
          <Text style={styles.date}>{formatDate(debt.created_at)}</Text>
        </View>
      </GlassCard>
    </View>
  );
}

const styles = StyleSheet.create({
  wrap: { marginBottom: spacing.sm },
  content: { padding: spacing.s },
  row: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 2 },
  amount: { ...type.amount, flex: 1, marginRight: 2 },
  description: { ...type.caption, color: colors.slate500, marginBottom: 1 },
  date: { ...type.caption, fontSize: 10, color: colors.slate400 },
});
