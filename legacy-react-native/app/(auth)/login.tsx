import { useState } from 'react';
import { StyleSheet, KeyboardAvoidingView, Platform, ScrollView, View } from 'react-native';
import { Text } from 'react-native-paper';
import { SafeAreaView } from 'react-native-safe-area-context';
import { LinearGradient } from 'expo-linear-gradient';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { router, Link } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { StatusBar } from 'expo-status-bar';
import { loginSchema } from '../../src/utils/validation';
import { signIn } from '../../src/services/auth.service';
import GlassBackground from '../../src/components/GlassBackground';
import GlassCard from '../../src/components/GlassCard';
import { GlassInput, PrimaryButton } from '../../src/components/FormControls';
import { colors, type, radius, spacing, blueGradient, heroShadow } from '../../src/theme';

type FormData = { email: string; password: string };

export default function LoginScreen() {
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  const { control, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  });

  const onSubmit = async ({ email, password }: FormData) => {
    setIsLoading(true);
    setErrorMsg('');
    try {
      await signIn(email, password);
      router.replace('/(tabs)');
    } catch {
      setErrorMsg("Email yoki parol noto'g'ri.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <GlassBackground>
      <SafeAreaView style={styles.safe} edges={['top']}>
        <StatusBar style="dark" />
        <KeyboardAvoidingView style={styles.flex} behavior={Platform.OS === 'ios' ? 'padding' : 'height'}>
          <ScrollView contentContainerStyle={styles.scroll} keyboardShouldPersistTaps="handled">
            <View style={styles.brand}>
              <LinearGradient colors={blueGradient} style={styles.logo}>
                <Ionicons name="receipt-outline" size={34} color={colors.white} />
              </LinearGradient>
              <Text style={styles.appName}>Hisobim</Text>
              <Text style={styles.tagline}>Do'koningiz uchun raqamli daftar</Text>
            </View>

            <GlassCard style={styles.card}>
              <View style={styles.cardInner}>
                <Text style={styles.cardTitle}>Kirish</Text>
                <Text style={styles.cardSub}>Email va parolingizni kiriting</Text>

                <Controller
                  control={control}
                  name="email"
                  render={({ field: { onChange, value } }) => (
                    <GlassInput
                      label="Email"
                      placeholder="misol@hisobim.uz"
                      autoCapitalize="none"
                      keyboardType="email-address"
                      value={value}
                      onChangeText={onChange}
                      error={errors.email?.message}
                    />
                  )}
                />

                <Controller
                  control={control}
                  name="password"
                  render={({ field: { onChange, value } }) => (
                    <GlassInput
                      label="Parol"
                      secureTextEntry
                      value={value}
                      onChangeText={onChange}
                      error={errors.password?.message}
                    />
                  )}
                />

                {errorMsg ? <Text style={styles.errorMsg}>{errorMsg}</Text> : null}

                <PrimaryButton label="Kirish" onPress={handleSubmit(onSubmit)} loading={isLoading} />

                <View style={styles.footer}>
                  <Text style={styles.footerText}>Hisobingiz yo'qmi? </Text>
                  <Link href="/(auth)/register" style={styles.footerLink}>Ro'yxatdan o'tish</Link>
                </View>
              </View>
            </GlassCard>
          </ScrollView>
        </KeyboardAvoidingView>
      </SafeAreaView>
    </GlassBackground>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1 },
  flex: { flex: 1 },
  scroll: { flexGrow: 1, padding: spacing.lg, justifyContent: 'center' },
  brand: { alignItems: 'center', marginBottom: spacing.xxl },
  logo: {
    width: 72,
    height: 72,
    borderRadius: radius.hero,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: spacing.lg,
    ...heroShadow,
  },
  appName: { ...type.display, fontSize: 30 },
  tagline: { ...type.caption, marginTop: spacing.sm },
  card: {},
  cardInner: { padding: spacing.xxl },
  cardTitle: { ...type.h1, marginBottom: spacing.xs },
  cardSub: { ...type.caption, marginBottom: spacing.xl },
  errorMsg: { ...type.caption, color: colors.debtRed, marginBottom: spacing.m },
  footer: { flexDirection: 'row', justifyContent: 'center', marginTop: spacing.xl },
  footerText: { ...type.caption },
  footerLink: { ...type.label, color: colors.accentBlue600 },
});
