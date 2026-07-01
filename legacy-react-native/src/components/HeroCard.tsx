import React from 'react';
import { StyleSheet, View, ViewStyle, StyleProp } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { blueGradient, radius, heroShadow } from '../theme';

type Props = {
  children: React.ReactNode;
  style?: StyleProp<ViewStyle>;
  borderRadius?: number;
};

/**
 * Hero karta — to'liq ko'k gradient + yumaloq yorug' dog'.
 * Har ekranda FAQAT bitta hero ishlatiladi (asosiy urg'u).
 */
export default function HeroCard({ children, style, borderRadius = radius.hero }: Props) {
  return (
    <View style={[heroShadow, { borderRadius }, style]}>
      <LinearGradient
        colors={blueGradient}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={[styles.gradient, { borderRadius }]}
      >
        {/* Radial yorug' dog' imitatsiyasi */}
        <View style={styles.highlight} />
        {/* Yuqori qirradagi inset highlight */}
        <View style={[styles.topLine, { borderTopLeftRadius: borderRadius, borderTopRightRadius: borderRadius }]} />
        {children}
      </LinearGradient>
    </View>
  );
}

const styles = StyleSheet.create({
  gradient: { overflow: 'hidden' },
  highlight: {
    position: 'absolute',
    top: -60,
    right: -40,
    width: 180,
    height: 180,
    borderRadius: 90,
    backgroundColor: 'rgba(255,255,255,0.16)',
  },
  topLine: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: 1,
    backgroundColor: 'rgba(255,255,255,0.5)',
  },
});
