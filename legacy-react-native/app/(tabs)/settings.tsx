import { useState } from 'react';
import { View, StyleSheet, Alert, ScrollView, TextInput, TouchableOpacity, Image } from 'react-native';
import { Text, ActivityIndicator } from 'react-native-paper';
import { router } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useAuthStore } from '../../src/stores/auth.store';
import { useShopStore } from '../../src/stores/shop.store';
import { signOut } from '../../src/services/auth.service';
import { updateShopName } from '../../src/services/shop.service';
import GlassBackground from '../../src/components/GlassBackground';
import GlassCard from '../../src/components/GlassCard';
import HeroCard from '../../src/components/HeroCard';
import ConfirmDialog from '../../src/components/ConfirmDialog';
import { colors, type, radius, spacing, fonts } from '../../src/theme';

const APP_VERSION = '1.0.0';

export default function SettingsScreen() {
  const { user, clear: clearAuth } = useAuthStore();
  const { activeShop, setShop } = useShopStore();

  const [editingName, setEditingName] = useState(false);
  const [shopName, setShopName] = useState(activeShop?.name ?? '');
  const [saving, setSaving] = useState(false);
  const [confirmLogout, setConfirmLogout] = useState(false);

  const handleSaveName = async () => {
    if (!activeShop || !shopName.trim()) return;
    setSaving(true);
    try {
      const updated = await updateShopName(activeShop.id, shopName.trim());
      setShop(updated);
      setEditingName(false);
    } catch {
      Alert.alert('Xatolik', "Do'kon nomini saqlashda xatolik yuz berdi");
    } finally {
      setSaving(false);
    }
  };

  const handleLogout = async () => {
    setConfirmLogout(false);
    try { await signOut(); } finally {
      clearAuth();
      setShop(null);
      router.replace('/(auth)/login');
    }
  };

  return (
    <GlassBackground>
      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false}>

        {/* Profil hero kartasi */}
        <HeroCard style={styles.hero}>
          <View style={styles.profileRow}>
            <View style={styles.profileAvatar}>
              <Image source={require('../../assets/owner-avatar.png')} style={styles.profileAvatarImg} />
            </View>
            <View style={styles.profileInfo}>
              <Text style={styles.profileEmail} numberOfLines={1}>{user?.email ?? '—'}</Text>
              <Text style={styles.profileRole}>Do'kon egasi</Text>
            </View>
          </View>
        </HeroCard>

        {/* Do'kon paneli */}
        <Text style={styles.sectionTitle}>DO'KON</Text>
        <GlassCard style={styles.card}>
          <View style={[styles.row, !editingName && styles.rowCenter]}>
            <View style={styles.rowIcon}>
              <Ionicons name="storefront-outline" size={20} color={colors.accentBlue600} />
            </View>
            <View style={styles.rowContent}>
              <Text style={styles.rowLabel}>Do'kon nomi</Text>
              {editingName ? (
                <View style={styles.editBlock}>
                  <TextInput
                    value={shopName}
                    onChangeText={setShopName}
                    style={styles.nameInput}
                    placeholderTextColor={colors.slate300}
                    autoFocus
                  />
                  <View style={styles.editActions}>
                    <TouchableOpacity
                      style={[styles.saveBtn, (!shopName.trim() || saving) && styles.saveBtnDisabled]}
                      onPress={handleSaveName}
                      disabled={!shopName.trim() || saving}
                    >
                      {saving ? (
                        <ActivityIndicator size={14} color={colors.white} />
                      ) : (
                        <Text style={styles.saveBtnText}>Saqlash</Text>
                      )}
                    </TouchableOpacity>
                    <TouchableOpacity
                      onPress={() => { setEditingName(false); setShopName(activeShop?.name ?? ''); }}
                    >
                      <Text style={styles.cancelText}>Bekor</Text>
                    </TouchableOpacity>
                  </View>
                </View>
              ) : (
                <Text style={styles.rowValue}>{activeShop?.name ?? '—'}</Text>
              )}
            </View>
            {!editingName && (
              <TouchableOpacity
                style={styles.editChip}
                activeOpacity={0.7}
                onPress={() => { setShopName(activeShop?.name ?? ''); setEditingName(true); }}
              >
                <Ionicons name="pencil" size={13} color={colors.accentBlue600} />
                <Text style={styles.editChipText}>Tahrirlash</Text>
              </TouchableOpacity>
            )}
          </View>

          <View style={[styles.row, styles.rowBorder]}>
            <View style={styles.rowIcon}>
              <Ionicons name="mail-outline" size={20} color={colors.accentBlue600} />
            </View>
            <View style={styles.rowContent}>
              <Text style={styles.rowLabel}>Email</Text>
              <Text style={styles.rowValue} numberOfLines={1}>{user?.email ?? '—'}</Text>
            </View>
          </View>
        </GlassCard>

        {/* Chiqish tugmasi */}
        <TouchableOpacity style={styles.logoutButton} onPress={() => setConfirmLogout(true)} activeOpacity={0.7}>
          <Ionicons name="log-out-outline" size={20} color={colors.debtRed} />
          <Text style={styles.logoutText}>Ilovadan chiqish</Text>
        </TouchableOpacity>

        <Text style={styles.version}>Hisobim v{APP_VERSION}</Text>

      </ScrollView>

      <ConfirmDialog
        visible={confirmLogout}
        title="Chiqish"
        message="Ilovadan chiqishni tasdiqlaysizmi?"
        confirmLabel="Chiqish"
        icon="log-out-outline"
        onConfirm={handleLogout}
        onDismiss={() => setConfirmLogout(false)}
      />
    </GlassBackground>
  );
}

const styles = StyleSheet.create({
  content: { padding: spacing.md, paddingBottom: 90 },

  hero: { marginBottom: spacing.lg },
  profileRow: { flexDirection: 'row', alignItems: 'center', gap: spacing.lg, padding: spacing.xl },
  profileAvatar: {
    width: 58,
    height: 58,
    borderRadius: radius.round,
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden',
  },
  profileAvatarImg: { width: '100%', height: '100%', transform: [{ scale: 1.4 }] },
  profileInfo: { flex: 1 },
  profileEmail: { ...type.h2, color: colors.white },
  profileRole: { ...type.caption, color: 'rgba(255,255,255,0.75)', marginTop: 2 },

  sectionTitle: { ...type.overline, marginBottom: spacing.s, marginLeft: spacing.xs },
  card: { marginBottom: spacing.lg },

  row: { flexDirection: 'row', alignItems: 'flex-start', padding: spacing.m, paddingHorizontal: spacing.lg },
  rowCenter: { alignItems: 'center' },
  rowBorder: { borderTopWidth: 1, borderTopColor: 'rgba(203,209,220,0.4)' },
  rowIcon: {
    width: 38,
    height: 38,
    borderRadius: radius.avatarBox,
    backgroundColor: 'rgba(47,111,214,0.10)',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: spacing.m,
    marginTop: 2,
  },
  rowContent: { flex: 1 },
  rowLabel: { ...type.label, marginBottom: 3 },
  rowValue: { ...type.body },
  editChip: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 5,
    marginLeft: spacing.m,
    backgroundColor: 'rgba(47,111,214,0.10)',
    borderWidth: 1,
    borderColor: 'rgba(47,111,214,0.18)',
    borderRadius: radius.round,
    paddingHorizontal: spacing.m,
    paddingVertical: spacing.sm,
  },
  editChipText: { ...type.label, color: colors.accentBlue600, letterSpacing: 0 },

  editBlock: { marginTop: spacing.xs },
  nameInput: {
    ...type.body,
    backgroundColor: 'rgba(255,255,255,0.6)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.8)',
    borderRadius: radius.input,
    paddingHorizontal: spacing.m,
    paddingVertical: spacing.s,
    marginBottom: spacing.s,
  },
  editActions: { flexDirection: 'row', alignItems: 'center', gap: spacing.m },
  saveBtn: {
    backgroundColor: colors.accentBlue600,
    borderRadius: radius.avatarBox,
    paddingHorizontal: spacing.lg,
    paddingVertical: spacing.s,
    minWidth: 80,
    alignItems: 'center',
  },
  saveBtnDisabled: { opacity: 0.5 },
  saveBtnText: { ...type.label, color: colors.white, letterSpacing: 0 },
  cancelText: { ...type.label, color: colors.slate400 },

  logoutButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: spacing.s,
    borderWidth: 1,
    borderColor: 'rgba(192,80,76,0.5)',
    borderRadius: radius.card,
    paddingVertical: spacing.lg,
    backgroundColor: 'rgba(192,80,76,0.04)',
  },
  logoutText: { ...type.body, fontFamily: fonts.bold, color: colors.debtRed },

  version: { ...type.caption, textAlign: 'center', color: colors.slate300, marginTop: spacing.lg },
});
