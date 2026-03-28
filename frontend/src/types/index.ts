export interface Product {
  id: number;
  name: string;
  category: string;
  price: number;
  stock: number;
  imageUrl: string;
}

export interface CartItem {
  product: Product;
  quantity: number;
}

export interface OrderItem {
  id: number;
  productId: number;
  quantity: number;
  unitPrice: number;
}

export interface Order {
  id: number;
  userId: number;
  total: number;
  status: string;
  items: OrderItem[];
  createdAt: string;
}

export interface Notification {
  _id: string;
  userId: string;
  message: string;
  status: string;
  type: string;
  createdAt: string;
}
