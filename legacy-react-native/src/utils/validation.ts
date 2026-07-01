import { z } from 'zod';

export const loginSchema = z.object({
  email: z.string().email("Email noto'g'ri formatda"),
  password: z.string().min(6, "Parol kamida 6 ta belgidan iborat bo'lishi kerak"),
});

export const registerSchema = z
  .object({
    email: z.string().email("Email noto'g'ri formatda"),
    password: z.string().min(6, "Parol kamida 6 ta belgidan iborat bo'lishi kerak"),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Parollar mos kelmadi",
    path: ["confirmPassword"],
  });

export const addCustomerSchema = z.object({
  name: z
    .string()
    .min(2, "Ism kamida 2 ta belgidan iborat bo'lishi kerak")
    .max(100, "Ism 100 ta belgidan oshmasligi kerak"),
  amount: z
    .string()
    .optional()
    .refine(
      (val) => !val || (!isNaN(Number(val)) && Number(val) > 0),
      "Summa 0 dan katta bo'lishi kerak"
    ),
  phone: z
    .string()
    .regex(/^\+?[0-9\s\-()]*$/, "Telefon raqam noto'g'ri formatda")
    .optional()
    .or(z.literal('')),
  note: z
    .string()
    .max(500, "Izoh 500 ta belgidan oshmasligi kerak")
    .optional(),
});

export const addDebtSchema = z.object({
  amount: z
    .string()
    .min(1, "Summani kiriting")
    .refine(
      (val) => !isNaN(Number(val)) && Number(val) > 0,
      "Summa 0 dan katta bo'lishi kerak"
    ),
  description: z.string().max(200, "Izoh 200 ta belgidan oshmasligi kerak").optional(),
});
