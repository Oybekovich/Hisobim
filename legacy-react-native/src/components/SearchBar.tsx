import React from 'react';
import { StyleSheet, View, TextInput } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import GlassCard from './GlassCard';
import { colors, type, radius, spacing, fonts } from '../theme';

type Props = {
  value: string;
  onChangeText: (text: string) => void;
  placeholder?: string;
};

export default function SearchBar({ value, onChangeText, placeholder }: Props) {
  return (
    <View style={styles.container}>
      <GlassCard variant="input" borderRadius={radius.input} shadow={false}>
        <View style={styles.row}>
          <Ionicons name="search" size={18} color={colors.slate400} />
          <TextInput
            placeholder={placeholder ?? 'Ism yoki telefon...'}
            placeholderTextColor={colors.slate300}
            value={value}
            onChangeText={onChangeText}
            style={styles.input}
          />
        </View>
      </GlassCard>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { paddingHorizontal: spacing.md, paddingVertical: spacing.s },
  row: { flexDirection: 'row', alignItems: 'center', paddingHorizontal: spacing.lg, height: 48, gap: spacing.s },
  input: { flex: 1, ...type.body, fontFamily: fonts.medium, padding: 0 },
});
