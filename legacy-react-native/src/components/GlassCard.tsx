import React from 'react';
import { StyleSheet, View, ViewStyle, StyleProp } from 'react-native';
import { BlurView } from 'expo-blur';
import { colors, radius, glass, softShadow, glassBorder } from '../theme';

type Variant = 'card' | 'input' | 'nav' | 'segment';

type Props = {
  children: React.ReactNode;
  variant?: Variant;
  style?: StyleProp<ViewStyle>;
  borderRadius?: number;
  shadow?: boolean;
};

/**
 * Liquid Glass karta — yarim shaffof oq + blur + nozik oq qirra.
 * Qatlamlar: blur → yarim shaffof oq → inset highlight → tashqi yumshoq soya.
 */
export default function GlassCard({
  children,
  variant = 'card',
  style,
  borderRadius = radius.card,
  shadow = true,
}: Props) {
  const preset = glass[variant];

  return (
    <View style={[shadow && softShadow, { borderRadius }, style]}>
      <View style={[styles.clip, { borderRadius }]}>
        <BlurView intensity={preset.blur * 4} tint="light" style={StyleSheet.absoluteFill} />
        <View
          style={[
            StyleSheet.absoluteFill,
            { backgroundColor: `rgba(255,255,255,${preset.opacity})` },
          ]}
        />
        {/* Yuqori qirradagi yorug' chiziq (shisha effekti) */}
        <View style={styles.topHighlight} />
        {children}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  clip: {
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: glassBorder,
    backgroundColor: 'rgba(255,255,255,0.55)',
  },
  topHighlight: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: 1,
    backgroundColor: 'rgba(255,255,255,0.85)',
  },
});
