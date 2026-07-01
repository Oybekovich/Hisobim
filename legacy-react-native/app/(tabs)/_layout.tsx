import { Tabs, Redirect } from 'expo-router';
import { ActivityIndicator, View, StyleSheet } from 'react-native';
import { BlurView } from 'expo-blur';
import { Ionicons } from '@expo/vector-icons';
import { useAuthStore } from '../../src/stores/auth.store';
import { colors, fonts, glass } from '../../src/theme';

export default function TabsLayout() {
  const { session, isLoading } = useAuthStore();

  if (isLoading) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: colors.bgTop }}>
        <ActivityIndicator size="large" color={colors.accentBlue600} />
      </View>
    );
  }

  if (!session) {
    return <Redirect href="/(auth)/login" />;
  }

  return (
    <Tabs
      screenOptions={{
        headerShown: true,
        tabBarActiveTintColor: colors.accentBlue600,
        tabBarInactiveTintColor: colors.slate200,
        tabBarStyle: {
          position: 'absolute',
          bottom: 6,
          backgroundColor: 'transparent',
          borderTopWidth: 0,
          elevation: 0,
          height: 64,
          paddingBottom: 10,
          paddingTop: 8,
        },
        tabBarBackground: () => (
          <BlurView
            intensity={glass.nav.blur * 4}
            tint="light"
            style={[StyleSheet.absoluteFill, styles.navGlass]}
          />
        ),
        tabBarLabelStyle: {
          fontFamily: fonts.semibold,
          fontSize: 9,
        },
        headerStyle: { backgroundColor: colors.bgTop },
        headerTitleStyle: { fontFamily: fonts.extrabold, fontSize: 18, color: colors.ink900 },
        headerShadowVisible: false,
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          headerShown: false,
          tabBarLabel: 'Mijozlar',
          tabBarIcon: ({ color, size }) => (
            <Ionicons name="people" color={color} size={size} />
          ),
        }}
      />
      <Tabs.Screen
        name="reports"
        options={{
          title: 'Hisobot',
          tabBarLabel: 'Hisobot',
          tabBarIcon: ({ color, size }) => (
            <Ionicons name="bar-chart-outline" color={color} size={size} />
          ),
        }}
      />
      <Tabs.Screen
        name="settings"
        options={{
          title: 'Sozlamalar',
          tabBarLabel: 'Sozlamalar',
          tabBarIcon: ({ color, size }) => (
            <Ionicons name="settings-outline" color={color} size={size} />
          ),
        }}
      />
      <Tabs.Screen name="search" options={{ href: null }} />
    </Tabs>
  );
}

const styles = StyleSheet.create({
  navGlass: {
    backgroundColor: `rgba(255,255,255,${glass.nav.opacity})`,
    borderTopWidth: 1,
    borderTopColor: 'rgba(255,255,255,0.7)',
  },
});
