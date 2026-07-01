import React from 'react';
import { StyleSheet, View, ViewStyle } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { colors } from '../theme';

type Props = {
  children: React.ReactNode;
  style?: ViewStyle;
};

/**
 * Liquid Glass fon — yumshoq gradient mesh.
 * Asos gradient + ikkita mayin rangli dog' (ko'k + binafsha) shishaga
 * "suyuqlik" beradi. Shisha kartalar shu fon ustida o'tiradi.
 */
export default function GlassBackground({ children, style }: Props) {
  return (
    <View style={[styles.root, style]}>
      <LinearGradient
        colors={[colors.bgTop, colors.bgBottom]}
        start={{ x: 0, y: 0 }}
        end={{ x: 0, y: 1 }}
        style={StyleSheet.absoluteFill}
      />
      {/* Ko'k mayin dog' (yuqori o'ng) */}
      <View style={[styles.blob, styles.blobBlue]} />
      {/* Binafsha mayin dog' (pastki chap) */}
      <View style={[styles.blob, styles.blobViolet]} />
      {children}
    </View>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, backgroundColor: colors.bgTop },
  blob: { position: 'absolute', width: 320, height: 320, borderRadius: 160, opacity: 0.45 },
  blobBlue: { top: -120, right: -90, backgroundColor: 'rgba(74, 134, 228, 0.20)' },
  blobViolet: { bottom: -110, left: -100, backgroundColor: 'rgba(122, 110, 214, 0.16)' },
});
