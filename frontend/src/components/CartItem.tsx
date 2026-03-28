import type { CartItem as CartItemType } from '../types';
import { useCart } from '../context/CartContext';

interface CartItemProps {
  item: CartItemType;
}

export default function CartItem({ item }: CartItemProps) {
  const { updateQuantity, removeFromCart } = useCart();
  const { product, quantity } = item;
  const lineTotal = product.price * quantity;

  return (
    <div className="d-flex align-items-center border-bottom py-3">
      <img
        src={product.imageUrl}
        alt={product.name}
        style={{ width: '80px', height: '80px', objectFit: 'cover' }}
        className="rounded me-3"
      />
      <div className="flex-grow-1">
        <h6 className="mb-1">{product.name}</h6>
        <p className="text-muted small mb-1">${product.price.toFixed(2)} each</p>
        <div className="d-flex align-items-center gap-2">
          <button
            className="btn btn-outline-secondary btn-sm"
            onClick={() => updateQuantity(product.id, quantity - 1)}
            aria-label={`Decrease quantity of ${product.name}`}
          >
            −
          </button>
          <span>{quantity}</span>
          <button
            className="btn btn-outline-secondary btn-sm"
            onClick={() => updateQuantity(product.id, quantity + 1)}
            aria-label={`Increase quantity of ${product.name}`}
          >
            +
          </button>
          <button
            className="btn btn-outline-danger btn-sm ms-2"
            onClick={() => removeFromCart(product.id)}
            aria-label={`Remove ${product.name} from cart`}
          >
            Remove
          </button>
        </div>
      </div>
      <div className="text-end fw-bold">
        ${lineTotal.toFixed(2)}
      </div>
    </div>
  );
}
