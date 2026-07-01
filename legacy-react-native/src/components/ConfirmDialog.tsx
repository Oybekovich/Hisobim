import React from 'react';
import { Modal, View, StyleSheet, TouchableOpacity, Pressable } from 'react-native';
import { Text } from 'react-native-paper';
import { LinearGradient } from 'expo-linear-gradient';
import { Ionicons } from '@expo/vector-icons';
import { colors, type, radius, spacing, blueGradient, heroShadow } from '../theme';

type Props = {
  visible: boolean;
  title: string;
  message: string;
  onConfirm: () => void;
  onDismiss: () => void;
  confirmLabel?: string;
  cancelLabel?: string;
  /** Qizil (o'chirish) yoki ko'k (oddiy) tasdiqlash. Standart: true */
  destructive?: boolean;
  /** Tepadagi ikonka (Ionicons nomi) */
  icon?: keyof typeof Ionicons.glyphMap;
};

const redGradient = ['#d4625e', colors.debtRed] as const;

export default function ConfirmDialog({
  visible,
  title,
  message,
  onConfirm,
  onDismiss,
  confirmLabel = "O'chirish",
  cancelLabel = 'Bekor qilish',
  destructive = true,
  icon,
}: Props) {
  const accent = destructive ? colors.debtRed : colors.accentBlue600;
  const iconName = icon ?? (destructive ? 'trash-outline' : 'help-circle-outline');

  return (
    <Modal visible={visible} transparent animationType="fade" onRequestClose={onDismiss} statusBarTranslucent>
      <Pressable style={styles.backdrop} onPress={onDismiss}>
        {/* Karta ustiga bosilganda yopilmasin */}
        <Pressable style={styles.card} onPress={() => {}}>
          <View style={[styles.iconWrap, { backgroundColor: `${accent}1f` }]}>
            <Ionicons name={iconName} size={26} color={accent} />
          </View>

          <Text style={styles.title}>{title}</Text>
          <Text style={styles.message}>{message}</Text>

          <View style={styles.actions}>
            <TouchableOpacity style={styles.cancelBtn} onPress={onDismiss} activeOpacity={0.7}>
              <Text style={styles.cancelText}>{cancelLabel}</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.confirmBtn} onPress={onConfirm} activeOpacity={0.88}>
              <LinearGradient colors={destructive ? redGradient : blueGradient} style={styles.confirmGradient}>
                <Text style={styles.confirmText}>{confirmLabel}</Text>
              </LinearGradient>
            </TouchableOpacity>
          </View>
        </Pressable>
      </Pressable>
    </Modal>
  );
}

const styles = StyleSheet.create({
  backdrop: {
    flex: 1,
    backgroundColor: 'rgba(18, 26, 44, 0.45)',
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: spacing.xl,
  },
  card: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: colors.white,
    borderRadius: radius.hero,
    paddingHorizontal: spacing.xl,
    paddingTop: spacing.xl,
    paddingBottom: spacing.lg,
    alignItems: 'center',
    ...heroShadow,
    shadowColor: 'rgba(18, 26, 44, 1)',
    shadowOpacity: 0.3,
  },
  iconWrap: {
    width: 54,
    height: 54,
    borderRadius: radius.round,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: spacing.m,
  },
  title: { ...type.h1, textAlign: 'center', marginBottom: spacing.sm },
  message: {
    ...type.label,
    fontFamily: type.body.fontFamily,
    color: colors.slate500,
    textAlign: 'center',
    lineHeight: 20,
    marginBottom: spacing.xl,
  },
  actions: { flexDirection: 'row', gap: spacing.m, alignSelf: 'stretch' },
  cancelBtn: {
    flex: 1,
    height: 50,
    borderRadius: radius.card,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(107, 115, 135, 0.10)',
  },
  cancelText: { ...type.h2, fontSize: 15, color: colors.ink700 },
  confirmBtn: { flex: 1, borderRadius: radius.card },
  confirmGradient: {
    height: 50,
    borderRadius: radius.card,
    justifyContent: 'center',
    alignItems: 'center',
  },
  confirmText: { ...type.h2, fontSize: 15, color: colors.white },
});
