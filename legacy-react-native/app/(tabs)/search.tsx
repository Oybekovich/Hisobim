import { useState } from 'react';
import { FlatList, StyleSheet } from 'react-native';
import { router } from 'expo-router';
import GlassBackground from '../../src/components/GlassBackground';
import { useShopStore } from '../../src/stores/shop.store';
import { searchCustomers } from '../../src/services/customer.service';
import CustomerCard from '../../src/components/CustomerCard';
import SearchBar from '../../src/components/SearchBar';
import EmptyState from '../../src/components/EmptyState';
import type { Customer } from '../../src/types';

export default function SearchScreen() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<Customer[]>([]);
  const [searched, setSearched] = useState(false);
  const { activeShop } = useShopStore();

  const handleSearch = async (text: string) => {
    setQuery(text);
    if (!activeShop || text.trim().length < 1) {
      setResults([]);
      setSearched(false);
      return;
    }
    try {
      const data = await searchCustomers(activeShop.id, text.trim());
      setResults(data);
      setSearched(true);
    } catch {
      setResults([]);
    }
  };

  return (
    <GlassBackground>
      <SearchBar value={query} onChangeText={handleSearch} placeholder="Ism yoki telefon..." />
      <FlatList
        data={results}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <CustomerCard
            customer={item}
            onPress={() => router.push(`/customer/${item.id}`)}
          />
        )}
        ListEmptyComponent={
          searched ? (
            <EmptyState message="Hech narsa topilmadi" />
          ) : (
            <EmptyState message="Qidirish uchun yozing" subMessage="Ism yoki telefon raqam" />
          )
        }
        contentContainerStyle={results.length === 0 ? styles.emptyList : styles.list}
        showsVerticalScrollIndicator={false}
      />
    </GlassBackground>
  );
}

const styles = StyleSheet.create({
  list: { paddingBottom: 90 },
  emptyList: { flexGrow: 1 },
});
