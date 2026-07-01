import { TextStyle, ViewStyle } from 'react-native';

/**
 * Hisobim — Liquid Glass Design System (v1.0)
 * Markaziy dizayn tokenlari: rang, shrift, spacing, radius va shisha presetlari.
 */

// ── Rang palitrasi ──────────────────────────────────────────────
export const colors = {
  // Brand (ko'k urg'u)
  accentBlue600: '#2f6fd6',
  accentBlue500: '#4a86e4',
  accentBlue700: '#2a62c4',
  accentBlueDeep: '#2a4fb0',

  // Neytral (ink & gray)
  ink900: '#1a2235',
  ink700: '#27304a',
  ink600: '#3a4258',
  slate500: '#6b7387',
  slate400: '#8b93a5',
  slate300: '#a8aebc',
  slate200: '#aeb4c2',
  slate100: '#cbd1dc',

  // Semantik
  debtRed: '#c0504c', // FAQAT qarz summasi (mayin terakota)
  paidGreen: '#5a8f6b', // juda kam ishlatiladi
  paidNeutral: '#b6bcc8', // "to'lagan" — toza, neytral

  // Fon
  bgTop: '#f4f7fc',
  bgBottom: '#fbfcfe',
  pageTop: '#f3f5f9',
  pageBottom: '#eceff4',

  white: '#ffffff',
} as const;

// Ko'k gradient (hero, asosiy tugma)
export const blueGradient = [colors.accentBlue500, colors.accentBlue600, colors.accentBlue700] as const;
export const deepBlueGradient = [colors.accentBlue600, colors.accentBlueDeep] as const;

// ── Shriftlar (Manrope) ─────────────────────────────────────────
export const fonts = {
  regular: 'Manrope_400Regular',
  medium: 'Manrope_500Medium',
  semibold: 'Manrope_600SemiBold',
  bold: 'Manrope_700Bold',
  extrabold: 'Manrope_800ExtraBold',
} as const;

// ── Tipografika presetlari ──────────────────────────────────────
export const type: Record<string, TextStyle> = {
  display: { fontFamily: fonts.extrabold, fontSize: 25, letterSpacing: -0.5, color: colors.ink900 },
  h1: { fontFamily: fonts.extrabold, fontSize: 18, letterSpacing: -0.4, color: colors.ink900 },
  h2: { fontFamily: fonts.bold, fontSize: 16, letterSpacing: -0.3, color: colors.ink900 },
  body: { fontFamily: fonts.bold, fontSize: 14, color: colors.ink900 },
  amount: { fontFamily: fonts.extrabold, fontSize: 13, color: colors.ink900 },
  label: { fontFamily: fonts.semibold, fontSize: 12, letterSpacing: 0.2, color: colors.slate500 },
  caption: { fontFamily: fonts.semibold, fontSize: 11, color: colors.slate400 },
  overline: {
    fontFamily: fonts.extrabold,
    fontSize: 10,
    letterSpacing: 1,
    color: colors.slate500,
    textTransform: 'uppercase',
  },
  nav: { fontFamily: fonts.semibold, fontSize: 9 },
};

// ── Spacing va radius ───────────────────────────────────────────
export const spacing = { xs: 4, sm: 6, s: 9, m: 12, md: 13, lg: 15, xl: 18, xxl: 22 } as const;

export const radius = {
  screen: 34,
  hero: 19,
  card: 17,
  input: 16,
  avatarBox: 11,
  fab: 16,
  segment: 14,
  round: 999,
} as const;

// ── Liquid Glass presetlari ─────────────────────────────────────
export const glass = {
  card: { opacity: 0.85, blur: 16 },
  input: { opacity: 0.82, blur: 16 },
  nav: { opacity: 0.8, blur: 22 },
  segment: { opacity: 0.78, blur: 16 },
} as const;

// Yumshoq ko'kimtir soya (keskin emas)
export const softShadow: ViewStyle = {
  shadowColor: 'rgba(40, 70, 130, 1)',
  shadowOffset: { width: 0, height: 5 },
  shadowOpacity: 0.08,
  shadowRadius: 16,
  elevation: 3,
};

export const heroShadow: ViewStyle = {
  shadowColor: colors.accentBlue700,
  shadowOffset: { width: 0, height: 10 },
  shadowOpacity: 0.28,
  shadowRadius: 22,
  elevation: 6,
};

// Shisha qirrasidagi nozik oq chegara
export const glassBorder = 'rgba(255, 255, 255, 0.72)';
