import React from 'react';
import {
  View,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  TextInputProps,
  ActivityIndicator,
} from 'react-native';
import { Text } from 'react-native-paper';
import { LinearGradient } from 'expo-linear-gradient';
import GlassCard from './GlassCard';
import { colors, type, radius, spacing, fonts, blueGradient, heroShadow } from '../theme';

// ── Glass input maydoni ─────────────────────────────────────────
type FieldProps = TextInputProps & {
  label: string;
  error?: string;
  suffix?: string;
};

export function GlassInput({ label, error, style, multiline, suffix, ...rest }: FieldProps) {
  return (
    <View style={styles.fieldWrap}>
      <Text style={styles.label}>{label}</Text>
      <GlassCard variant="input" borderRadius={radius.input} shadow={false}>
        <View style={styles.inputRow}>
          <TextInput
            placeholderTextColor={colors.slate300}
            multiline={multiline}
            style={[styles.input, multiline && styles.inputMultiline, suffix ? styles.inputWithSuffix : null, style]}
            {...rest}
          />
          {suffix ? <Text style={styles.suffix}>{suffix}</Text> : null}
        </View>
      </GlassCard>
      {error ? <Text style={styles.error}>{error}</Text> : null}
    </View>
  );
}

// ── Asosiy (Primary) tugma — ko'k gradient ──────────────────────
type ButtonProps = {
  label: string;
  onPress: () => void;
  loading?: boolean;
  disabled?: boolean;
  icon?: React.ReactNode;
};

export function PrimaryButton({ label, onPress, loading, disabled, icon }: ButtonProps) {
  return (
    <TouchableOpacity
      onPress={onPress}
      disabled={disabled || loading}
      activeOpacity={0.88}
      style={[styles.primaryWrap, (disabled || loading) && styles.disabled]}
    >
      <LinearGradient colors={blueGradient} style={styles.primary}>
        {loading ? (
          <ActivityIndicator color={colors.white} />
        ) : (
          <View style={styles.primaryContent}>
            {icon}
            <Text style={styles.primaryText}>{label}</Text>
          </View>
        )}
      </LinearGradient>
    </TouchableOpacity>
  );
}

// ── Segment toggle (Qarz / To'lov) ──────────────────────────────
type SegmentProps = {
  value: string;
  onChange: (v: string) => void;
  options: { value: string; label: string }[];
};

export function SegmentToggle({ value, onChange, options }: SegmentProps) {
  return (
    <GlassCard variant="segment" borderRadius={radius.segment} shadow={false}>
      <View style={styles.segment}>
        {options.map((opt) => {
          const active = opt.value === value;
          return (
            <TouchableOpacity
              key={opt.value}
              style={styles.segmentItem}
              activeOpacity={0.8}
              onPress={() => onChange(opt.value)}
            >
              {active ? (
                <LinearGradient colors={blueGradient} style={styles.segmentActive}>
                  <Text style={[styles.segmentText, styles.segmentTextActive]}>{opt.label}</Text>
                </LinearGradient>
              ) : (
                <Text style={styles.segmentText}>{opt.label}</Text>
              )}
            </TouchableOpacity>
          );
        })}
      </View>
    </GlassCard>
  );
}

const styles = StyleSheet.create({
  fieldWrap: { marginBottom: spacing.lg },
  label: { ...type.label, marginBottom: spacing.sm, marginLeft: spacing.xs },
  inputRow: { flexDirection: 'row', alignItems: 'center' },
  input: {
    flex: 1,
    ...type.body,
    fontFamily: fonts.medium,
    paddingHorizontal: spacing.lg,
    paddingVertical: spacing.m,
    minHeight: 50,
  },
  inputWithSuffix: { paddingRight: spacing.sm },
  suffix: {
    ...type.label,
    color: colors.slate400,
    fontSize: 13,
    paddingRight: spacing.lg,
  },
  inputMultiline: { minHeight: 84, textAlignVertical: 'top' },
  error: { ...type.caption, color: colors.debtRed, marginTop: spacing.xs, marginLeft: spacing.xs },

  primaryWrap: { borderRadius: radius.card, ...heroShadow },
  disabled: { opacity: 0.5 },
  primary: {
    height: 54,
    borderRadius: radius.card,
    justifyContent: 'center',
    alignItems: 'center',
  },
  primaryContent: { flexDirection: 'row', alignItems: 'center', gap: spacing.sm },
  primaryText: { ...type.h2, color: colors.white, fontSize: 15 },

  segment: { flexDirection: 'row', padding: spacing.xs, gap: spacing.xs },
  segmentItem: { flex: 1 },
  segmentActive: {
    borderRadius: radius.avatarBox,
    paddingVertical: spacing.m,
    alignItems: 'center',
  },
  segmentText: {
    ...type.label,
    color: colors.slate400,
    textAlign: 'center',
    paddingVertical: spacing.m,
    letterSpacing: 0,
  },
  segmentTextActive: { color: colors.white, paddingVertical: 0 },
});
