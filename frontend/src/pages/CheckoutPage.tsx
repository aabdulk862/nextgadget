import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import apiClient from '../api/client';

export default function CheckoutPage() {
  const { items, totalPrice, clearCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [placing, setPlacing] = useState(false);
  const [error, setError] = useState('');

  const handlePlaceOrder = async () => {
    if (!user || items.length === 0) return;
    setPlacing(true);
    setError('');
    try {
      await apiClient.post(
        '/api/orders',
        {
          items: items.map((item) => ({
            productId: item.product.id,
            quantity: item.quantity,
          })),
        },
        { headers: { 'X-User-Id': String(user.id) } },
      );
      clearCart();
      navigate('/orders');
    } catch {
      setError('Failed to place order. Please try again.');
    } finally {
      setPlacing(false);
    }
  };

  return (
    <main className="container py-4">
      <h1 className="mb-4">Checkout</h1>

      {items.length === 0 ? (
        <p className="text-muted">Your cart is empty.</p>
      ) : (
        <>
          <table className="table">
            <thead>
              <tr>
                <th>Product</th>
                <th>Qty</th>
                <th>Price</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <tr key={item.product.id}>
                  <td>{item.product.name}</td>
                  <td>{item.quantity}</td>
                  <td>${(item.product.price * item.quantity).toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="d-flex justify-content-between align-items-center mt-3">
            <h4 className="mb-0">Total: ${totalPrice.toFixed(2)}</h4>
            <button
              className="btn btn-primary btn-lg"
              onClick={handlePlaceOrder}
              disabled={placing}
            >
              {placing ? 'Placing Order...' : 'Place Order'}
            </button>
          </div>

          {error && (
            <div className="alert alert-danger mt-3" role="alert">
              {error}
            </div>
          )}
        </>
      )}
    </main>
  );
}
