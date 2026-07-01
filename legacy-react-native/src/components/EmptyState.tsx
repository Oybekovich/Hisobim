import React from 'react';
import { View, StyleSheet } from 'react-native';
import { Text } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';
import { colors, type, radius, spacing } from '../theme';

type Props = {
  message: string;
  subMessage?: string;
  icon?: keyof typeof Ionicons.glyphMap;
};

export default function EmptyState({ message, subMessage, icon = 'people-outline' }: Props) {
  return (
    <View style={styles.container}>
      <View style={styles.iconWrap}>
        <Ionicons name={icon} size={42} color={colors.slate300} />
      </View>
      <Text style={styles.message}>{message}</Text>
      {subMessage ? <Text style={styles.sub}>{subMessage}</Text> : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center', padding: 40 },
  iconWrap: {
    width: 84,
    height: 84,
    borderRadius: radius.round,
    backgroundColor: 'rgba(255,255,255,0.55)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.7)',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: spacing.xl,
  },
  message: { ...type.h2, color: colors.slate500, textAlign: 'center' },
  sub: { ...type.caption, textAlign: 'center', marginTop: spacing.sm },
});
