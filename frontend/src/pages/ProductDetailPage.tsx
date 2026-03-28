import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import apiClient from '../api/client';
import type { Product } from '../types';
import { useCart } from '../context/CartContext';
import LoadingSpinner from '../components/LoadingSpinner';

export default function ProductDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { addToCart } = useCart();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [quantity, setQuantity] = useState(1);
  const [added, setAdded] = useState(false);

  useEffect(() => {
    apiClient
      .get<Product>(`/api/products/${id}`)
      .then((res) => setProduct(res.data))
      .catch(() => setProduct(null))
      .finally(() => setLoading(false));
  }, [id]);

  const handleAddToCart = () => {
    if (!product) return;
    addToCart(product, quantity);
    setAdded(true);
    setTimeout(() => setAdded(false), 2000);
  };

  if (loading) return <LoadingSpinner />;

  if (!product) {
    return (
      <main className="container py-5 text-center">
        <p className="text-muted">Product not found.</p>
      </main>
    );
  }

  return (
    <main className="container py-4">
      <div className="row">
        <div className="col-md-6 mb-4">
          <img
            src={product.imageUrl}
            alt={`${product.name} - ${product.category}`}
            className="img-fluid rounded"
          />
        </div>
        <div className="col-md-6">
          <span className="badge bg-secondary mb-2">{product.category}</span>
          <h1>{product.name}</h1>
          <p className="fs-4 fw-bold">${product.price.toFixed(2)}</p>
          <p className="text-muted">
            {product.stock > 0 ? `${product.stock} in stock` : 'Out of stock'}
          </p>

          <div className="d-flex align-items-center gap-3 mt-4">
            <label htmlFor="qty" className="form-label mb-0">Qty:</label>
            <input
              id="qty"
              type="number"
              className="form-control"
              style={{ width: '80px' }}
              min={1}
              max={product.stock}
              value={quantity}
              onChange={(e) => setQuantity(Math.max(1, Number(e.target.value)))}
            />
            <button
              className="btn btn-primary"
              onClick={handleAddToCart}
              disabled={product.stock <= 0}
            >
              Add to Cart
            </button>
          </div>
          {added && (
            <div className="alert alert-success mt-3" role="alert">
              Added to cart!
            </div>
          )}
        </div>
      </div>
    </main>
  );
}
