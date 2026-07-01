import 'react-native-url-polyfill/auto';
import { useEffect } from 'react';
import { View } from 'react-native';
import { Stack } from 'expo-router';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { PaperProvider, MD3LightTheme, configureFonts, ActivityIndicator } from 'react-native-paper';
import { StatusBar } from 'expo-status-bar';
import {
  useFonts,
  Manrope_400Regular,
  Manrope_500Medium,
  Manrope_600SemiBold,
  Manrope_700Bold,
  Manrope_800ExtraBold,
} from '@expo-google-fonts/manrope';
import { supabase } from '../src/lib/supabase';
import { useAuthStore } from '../src/stores/auth.store';
import { colors, fonts } from '../src/theme';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      staleTime: 0,
    },
  },
});

const fontConfig = {
  default: { fontFamily: fonts.regular, fontWeight: '400' as const, letterSpacing: 0 },
};

const hisobimTheme = {
  ...MD3LightTheme,
  fonts: configureFonts({ config: fontConfig.default }),
  colors: {
    ...MD3LightTheme.colors,
    primary: colors.accentBlue600,
    secondary: colors.accentBlue500,
    error: colors.debtRed,
    background: colors.bgTop,
  },
};

export default function RootLayout() {
  const { setSession } = useAuthStore();
  const [fontsLoaded] = useFonts({
    Manrope_400Regular,
    Manrope_500Medium,
    Manrope_600SemiBold,
    Manrope_700Bold,
    Manrope_800ExtraBold,
  });

  useEffect(() => {
    supabase.auth.getSession().then(({ data: { session } }) => {
      setSession(session);
    });

    const {
      data: { subscription },
    } = supabase.auth.onAuthStateChange((_event, session) => {
      setSession(session);
    });

    return () => subscription.unsubscribe();
  }, []);

  if (!fontsLoaded) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: colors.bgTop }}>
        <ActivityIndicator size="large" color={colors.accentBlue600} />
      </View>
    );
  }

  return (
    <QueryClientProvider client={queryClient}>
      <PaperProvider theme={hisobimTheme}>
        <StatusBar style="dark" />
        <Stack screenOptions={{
          headerShown: false,
          headerStyle: { backgroundColor: colors.bgTop },
          headerTitleStyle: { fontFamily: fonts.extrabold, fontSize: 18, color: colors.ink900 },
          headerTintColor: colors.accentBlue600,
          headerShadowVisible: false,
          headerBackTitle: '',
        }}>
          <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
          <Stack.Screen name="(auth)/login" options={{ headerShown: false }} />
          <Stack.Screen name="(auth)/register" options={{ headerShown: false }} />
          <Stack.Screen name="customer/[id]" options={{ headerShown: true }} />
          <Stack.Screen name="customer/add" options={{ headerShown: false }} />
          <Stack.Screen name="debt/add" options={{ headerShown: true, title: "Yozuv qo'shish" }} />
          <Stack.Screen name="customer/edit" options={{ headerShown: true }} />
        </Stack>
      </PaperProvider>
    </QueryClientProvider>
  );
}
