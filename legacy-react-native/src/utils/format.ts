const MONTHS = ['Yan','Feb','Mar','Apr','May','Iyn','Iyl','Avg','Sen','Okt','Noy','Dek'];

export function formatAmount(amount: number): string {
  const abs = Math.abs(amount)
    .toLocaleString('ru-RU')
    .replace(',', '.');
  const sign = amount < 0 ? '-' : '';
  return `${sign}${abs} so'm`;
}

/**
 * O'zbekiston telefon raqamini formatlaydi: "+998 99-666-88-99".
 * +998 avtomatik qo'shiladi, qolgan 9 raqam XX-XXX-XX-XX ko'rinishida ajratiladi.
 */
export function formatUzPhone(input: string): string {
  let digits = input.replace(/\D/g, '');
  if (digits.startsWith('998')) digits = digits.slice(3);
  digits = digits.slice(0, 9);
  if (digits.length === 0) return '';

  const parts = [digits.slice(0, 2)];
  if (digits.length > 2) parts.push(digits.slice(2, 5));
  if (digits.length > 5) parts.push(digits.slice(5, 7));
  if (digits.length > 7) parts.push(digits.slice(7, 9));

  return `+998 ${parts.join('-')}`;
}

export function formatDate(isoString: string): string {
  const d = new Date(isoString);
  const now = new Date();
  const isThisYear = d.getFullYear() === now.getFullYear();
  const base = `${d.getDate()} ${MONTHS[d.getMonth()]}`;
  return isThisYear ? base : `${base} ${d.getFullYear()}`;
}
