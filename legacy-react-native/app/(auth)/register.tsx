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
import { registerSchema } from '../../src/utils/validation';
import { signUp, getSession } from '../../src/services/auth.service';
import GlassBackground from '../../src/components/GlassBackground';
import GlassCard from '../../src/components/GlassCard';
import { GlassInput, PrimaryButton } from '../../src/components/FormControls';
import { colors, type, radius, spacing, blueGradient, heroShadow } from '../../src/theme';

type FormData = { email: string; password: string; confirmPassword: string };

export default function RegisterScreen() {
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [infoMsg, setInfoMsg] = useState('');

  const { control, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: { email: '', password: '', confirmPassword: '' },
  });

  const onSubmit = async ({ email, password }: FormData) => {
    setIsLoading(true);
    setErrorMsg('');
    setInfoMsg('');
    try {
      await signUp(email, password);
      const session = await getSession();
      if (session) {
        router.replace('/(tabs)');
      } else {
        setInfoMsg('Hisob yaratildi. Endi kirish sahifasidan kiring.');
      }
    } catch {
      setErrorMsg("Ro'yxatdan o'tishda xatolik. Bu email band bo'lishi mumkin.");
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
                <Ionicons name="person-add-outline" size={32} color={colors.white} />
              </LinearGradient>
              <Text style={styles.appName}>Ro'yxatdan o'tish</Text>
              <Text style={styles.tagline}>Yangi do'kon hisobini oching</Text>
            </View>

            <GlassCard style={styles.card}>
              <View style={styles.cardInner}>
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

                <Controller
                  control={control}
                  name="confirmPassword"
                  render={({ field: { onChange, value } }) => (
                    <GlassInput
                      label="Parolni tasdiqlang"
                      secureTextEntry
                      value={value}
                      onChangeText={onChange}
                      error={errors.confirmPassword?.message}
                    />
                  )}
                />

                {errorMsg ? <Text style={styles.errorMsg}>{errorMsg}</Text> : null}
                {infoMsg ? <Text style={styles.infoMsg}>{infoMsg}</Text> : null}

                <PrimaryButton label="Ro'yxatdan o'tish" onPress={handleSubmit(onSubmit)} loading={isLoading} />

                <View style={styles.footer}>
                  <Text style={styles.footerText}>Hisobingiz bormi? </Text>
                  <Link href="/(auth)/login" style={styles.footerLink}>Kirish</Link>
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
  brand: { alignItems: 'center', marginBottom: spacing.xl },
  logo: {
    width: 68,
    height: 68,
    borderRadius: radius.hero,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: spacing.m,
    ...heroShadow,
  },
  appName: { ...type.display, fontSize: 24 },
  tagline: { ...type.caption, marginTop: spacing.sm },
  card: {},
  cardInner: { padding: spacing.xxl },
  errorMsg: { ...type.caption, color: colors.debtRed, marginBottom: spacing.m },
  infoMsg: { ...type.caption, color: colors.accentBlue600, marginBottom: spacing.m },
  footer: { flexDirection: 'row', justifyContent: 'center', marginTop: spacing.xl },
  footerText: { ...type.caption },
  footerLink: { ...type.label, color: colors.accentBlue600 },
});
