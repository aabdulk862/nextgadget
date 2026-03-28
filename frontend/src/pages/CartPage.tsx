import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import CartItem from '../components/CartItem';

export default function CartPage() {
  const { items, totalPrice } = useCart();
  const { isAuthenticated } = useAuth();

  return (
    <main className="container py-4">
      <h1 className="mb-4">Shopping Cart</h1>

      {items.length === 0 ? (
        <div className="text-center py-5">
          <p className="text-muted">Your cart is empty.</p>
          <Link to="/products" className="btn btn-primary">
            Browse Products
          </Link>
        </div>
      ) : (
        <>
          {items.map((item) => (
            <CartItem key={item.product.id} item={item} />
          ))}
          <div className="d-flex justify-content-between align-items-center mt-4 pt-3 border-top">
            <h4 className="mb-0">Total: ${totalPrice.toFixed(2)}</h4>
            <Link
              to={isAuthenticated ? '/checkout' : '/login'}
              className="btn btn-primary btn-lg"
            >
              Proceed to Checkout
            </Link>
          </div>
        </>
      )}
    </main>
  );
}
