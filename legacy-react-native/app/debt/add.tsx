import { useState } from 'react';
import { StyleSheet, KeyboardAvoidingView, Platform, ScrollView, View } from 'react-native';
import { Text } from 'react-native-paper';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useLocalSearchParams, router, Stack } from 'expo-router';
import { addDebtSchema } from '../../src/utils/validation';
import { useCreateDebt } from '../../src/hooks/useDebts';
import { useAuthStore } from '../../src/stores/auth.store';
import { useShopStore } from '../../src/stores/shop.store';
import { formatAmount } from '../../src/utils/format';
import GlassBackground from '../../src/components/GlassBackground';
import { GlassInput, PrimaryButton, SegmentToggle } from '../../src/components/FormControls';
import { colors, type, spacing } from '../../src/theme';

type EntryType = 'debt' | 'payment';
type FormData = { amount: string; description?: string };

export default function AddDebtScreen() {
  const { customerId } = useLocalSearchParams<{ customerId: string }>();
  const [entryType, setEntryType] = useState<EntryType>('debt');
  const { user } = useAuthStore();
  const { activeShop } = useShopStore();
  const createDebt = useCreateDebt(customerId, user?.id ?? '');
  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<FormData>({
    resolver: zodResolver(addDebtSchema),
    defaultValues: { amount: '', description: '' },
  });

  const rawAmount = watch('amount');
  const parsedAmount = Number(rawAmount);
  const showPreview = rawAmount && !isNaN(parsedAmount) && parsedAmount > 0;
  const displayAmount = showPreview
    ? formatAmount(entryType === 'payment' ? -parsedAmount : parsedAmount)
    : '';

  const onSubmit = async ({ amount, description }: FormData) => {
    if (!activeShop || !customerId) return;
    const numeric = Number(amount);
    const finalAmount = entryType === 'payment' ? -numeric : numeric;
    await createDebt.mutateAsync({
      shop_id: activeShop.id,
      customer_id: customerId,
      amount: finalAmount,
      description: description || null,
    });
    router.back();
  };

  return (
    <>
      <Stack.Screen options={{ title: "Yozuv qo'shish" }} />
      <GlassBackground>
        <KeyboardAvoidingView
          style={styles.flex}
          behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        >
          <ScrollView contentContainerStyle={styles.container} keyboardShouldPersistTaps="handled">
            <View style={styles.segmentWrap}>
              <SegmentToggle
                value={entryType}
                onChange={(v) => setEntryType(v as EntryType)}
                options={[
                  { value: 'debt', label: 'Qarz qildi' },
                  { value: 'payment', label: "To'lov qildi" },
                ]}
              />
            </View>

            <Controller
              control={control}
              name="amount"
              render={({ field: { onChange, value } }) => (
                <GlassInput
                  label="Summa (so'm)"
                  keyboardType="numeric"
                  value={value}
                  onChangeText={(t) => onChange(t.replace(/[^0-9]/g, ''))}
                  error={errors.amount?.message}
                  placeholder="0"
                />
              )}
            />

            {showPreview ? (
              <Text
                style={[
                  styles.preview,
                  { color: entryType === 'payment' ? colors.paidGreen : colors.debtRed },
                ]}
              >
                {displayAmount}
              </Text>
            ) : null}

            <Controller
              control={control}
              name="description"
              render={({ field: { onChange, value } }) => (
                <GlassInput
                  label="Izoh (ixtiyoriy)"
                  value={value}
                  onChangeText={onChange}
                  placeholder="Qo'shimcha izoh"
                />
              )}
            />

            <PrimaryButton
              label="Saqlash"
              onPress={handleSubmit(onSubmit)}
              loading={createDebt.isPending}
            />
          </ScrollView>
        </KeyboardAvoidingView>
      </GlassBackground>
    </>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  container: { padding: spacing.lg, paddingTop: spacing.xl },
  segmentWrap: { marginBottom: spacing.xl },
  preview: { ...type.display, textAlign: 'center', marginBottom: spacing.lg, marginTop: -spacing.s },
});
