import { useState } from 'react';
import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet } from 'react-native';
import { useLocalSearchParams, router, Stack } from 'expo-router';
import { useCustomerById, useUpdateCustomer } from '../../src/hooks/useCustomers';
import { useShopStore } from '../../src/stores/shop.store';
import GlassBackground from '../../src/components/GlassBackground';
import { GlassInput, PrimaryButton } from '../../src/components/FormControls';
import { formatUzPhone } from '../../src/utils/format';
import { spacing } from '../../src/theme';

export default function CustomerEditScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { activeShop } = useShopStore();
  const { data: customer } = useCustomerById(id, activeShop?.id);
  const updateCustomer = useUpdateCustomer(activeShop?.id ?? '', id);

  const [name, setName] = useState(customer?.name ?? '');
  const [phone, setPhone] = useState(formatUzPhone(customer?.phone ?? ''));
  const [note, setNote] = useState(customer?.note ?? '');

  const nameError = name.trim().length === 0;

  const handleSave = () => {
    if (nameError) return;
    updateCustomer.mutate(
      { name: name.trim(), phone: phone.trim() || null, note: note.trim() || null },
      { onSuccess: () => router.back() }
    );
  };

  return (
    <>
      <Stack.Screen options={{ title: 'Mijozni tahrirlash' }} />
      <GlassBackground>
        <KeyboardAvoidingView
          style={styles.flex}
          behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        >
          <ScrollView contentContainerStyle={styles.container} keyboardShouldPersistTaps="handled">
            <GlassInput
              label="Ism *"
              value={name}
              onChangeText={setName}
              error={nameError && name.length > 0 ? 'Ism kiritilishi shart' : undefined}
              autoFocus
              placeholder="Mijoz ismi"
            />

            <GlassInput
              label="Telefon raqam"
              value={phone}
              onChangeText={(t) => setPhone(formatUzPhone(t))}
              keyboardType="phone-pad"
              placeholder="+998 90-123-45-67"
            />

            <GlassInput
              label="Izoh"
              value={note}
              onChangeText={setNote}
              multiline
              placeholder="Qo'shimcha izoh"
            />

            <PrimaryButton
              label="Saqlash"
              onPress={handleSave}
              loading={updateCustomer.isPending}
              disabled={nameError}
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
});
