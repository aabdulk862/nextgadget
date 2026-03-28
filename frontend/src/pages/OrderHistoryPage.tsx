import { useEffect, useState } from 'react';
import apiClient from '../api/client';
import { useAuth } from '../context/AuthContext';
import type { Order } from '../types';
import OrderSummary from '../components/OrderSummary';
import LoadingSpinner from '../components/LoadingSpinner';

export default function OrderHistoryPage() {
  const { user } = useAuth();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) return;
    apiClient
      .get<Order[]>(`/api/orders/user/${user.id}`)
      .then((res) => setOrders(res.data))
      .catch(() => setOrders([]))
      .finally(() => setLoading(false));
  }, [user]);

  return (
    <main className="container py-4">
      <h1 className="mb-4">Order History</h1>

      {loading ? (
        <LoadingSpinner />
      ) : orders.length === 0 ? (
        <p className="text-muted">You have no orders yet.</p>
      ) : (
        orders.map((order) => <OrderSummary key={order.id} order={order} />)
      )}
    </main>
  );
}
