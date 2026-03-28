import { Link } from 'react-router-dom';
import type { Product } from '../types';

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  return (
    <div className="col-sm-6 col-lg-4 col-xl-3 mb-4">
      <div className="card product-card h-100">
        <div className="overflow-hidden">
          <img
            src={product.imageUrl}
            className="card-img-top"
            alt={`${product.name} - ${product.category}`}
          />
        </div>
        <div className="card-body d-flex flex-column">
          <span className="badge bg-secondary mb-2 align-self-start">{product.category}</span>
          <h5 className="card-title">{product.name}</h5>
          <p className="card-text fw-bold">${product.price.toFixed(2)}</p>
          <p className="card-text text-muted small">
            {product.stock > 0 ? `${product.stock} in stock` : 'Out of stock'}
          </p>
          <Link to={`/products/${product.id}`} className="btn btn-primary mt-auto">
            View Details
          </Link>
        </div>
      </div>
    </div>
  );
}
