import { StyleSheet, KeyboardAvoidingView, Platform, ScrollView, View, TouchableOpacity } from 'react-native';
import { Text } from 'react-native-paper';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useQueryClient } from '@tanstack/react-query';
import { router, Stack } from 'expo-router';
import { addCustomerSchema } from '../../src/utils/validation';
import { useCreateCustomer } from '../../src/hooks/useCustomers';
import { createDebt } from '../../src/services/debt.service';
import { formatUzPhone } from '../../src/utils/format';
import { useAuthStore } from '../../src/stores/auth.store';
import { useShopStore } from '../../src/stores/shop.store';
import GlassBackground from '../../src/components/GlassBackground';
import { GlassInput, PrimaryButton } from '../../src/components/FormControls';
import { colors, type, radius, spacing } from '../../src/theme';

type FormData = { name: string; amount?: string; phone?: string; note?: string };

// Raqamlarni har 3 xonada bo'sh joy bilan ajratadi: "600000" -> "600 000"
function groupThousands(digits: string): string {
  return digits.replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
}

export default function AddCustomerScreen() {
  const { user } = useAuthStore();
  const { activeShop } = useShopStore();
  const queryClient = useQueryClient();
  const createCustomer = useCreateCustomer(activeShop?.id ?? '');

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<FormData>({
    resolver: zodResolver(addCustomerSchema),
    defaultValues: { name: '', amount: '', phone: '', note: '' },
  });

  const onSubmit = async ({ name, amount, phone, note }: FormData) => {
    if (!activeShop) return;
    const customer = await createCustomer.mutateAsync({
      shop_id: activeShop.id,
      name,
      phone: phone || null,
      note: note || null,
    });

    const numericAmount = Number(amount);
    if (amount && numericAmount > 0 && user) {
      await createDebt(
        {
          shop_id: activeShop.id,
          customer_id: customer.id,
          amount: numericAmount,
          description: "Boshlang'ich qarz",
        },
        user.id
      );
      queryClient.invalidateQueries({ queryKey: ['customers'] });
      queryClient.invalidateQueries({ queryKey: ['reports'] });
    }

    router.back();
  };

  return (
    <>
      <Stack.Screen options={{ headerShown: false }} />
      <GlassBackground>
        <SafeAreaView style={styles.flex} edges={['top']}>
          <View style={styles.header}>
            <TouchableOpacity onPress={() => router.back()} style={styles.backBtn} activeOpacity={0.7}>
              <Ionicons name="chevron-back" size={24} color={colors.ink900} />
            </TouchableOpacity>
            <Text style={styles.title}>Mijoz qo'shish</Text>
          </View>

          <KeyboardAvoidingView
            style={styles.flex}
            behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
          >
            <ScrollView contentContainerStyle={styles.container} keyboardShouldPersistTaps="handled">
            <Controller
              control={control}
              name="name"
              render={({ field: { onChange, value } }) => (
                <GlassInput
                  label="Ism *"
                  value={value}
                  onChangeText={onChange}
                  error={errors.name?.message}
                  placeholder="Mijoz ismi"
                />
              )}
            />

            <Controller
              control={control}
              name="amount"
              render={({ field: { onChange, value } }) => (
                <GlassInput
                  label="Qarz summasi (ixtiyoriy)"
                  keyboardType="numeric"
                  value={groupThousands(value ?? '')}
                  onChangeText={(t) => onChange(t.replace(/[^0-9]/g, ''))}
                  error={errors.amount?.message}
                  placeholder="0"
                  suffix="UZS"
                />
              )}
            />

            <Controller
              control={control}
              name="phone"
              render={({ field: { onChange, value } }) => (
                <GlassInput
                  label="Telefon (ixtiyoriy)"
                  keyboardType="phone-pad"
                  value={value}
                  onChangeText={(t) => onChange(formatUzPhone(t))}
                  error={errors.phone?.message}
                  placeholder="+998 90-123-45-67"
                />
              )}
            />

            <Controller
              control={control}
              name="note"
              render={({ field: { onChange, value } }) => (
                <GlassInput
                  label="Izoh (ixtiyoriy)"
                  value={value}
                  onChangeText={onChange}
                  multiline
                  placeholder="Qo'shimcha izoh"
                />
              )}
            />

              <PrimaryButton
                label="Saqlash"
                onPress={handleSubmit(onSubmit)}
                loading={createCustomer.isPending}
              />
            </ScrollView>
          </KeyboardAvoidingView>
        </SafeAreaView>
      </GlassBackground>
    </>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: spacing.lg,
    paddingTop: spacing.xxl,
    paddingBottom: spacing.m,
  },
  backBtn: {
    width: 38,
    height: 38,
    borderRadius: radius.round,
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: -spacing.s,
    marginRight: spacing.xs,
  },
  title: { ...type.h1 },
  container: { padding: spacing.lg, paddingTop: spacing.m },
});
