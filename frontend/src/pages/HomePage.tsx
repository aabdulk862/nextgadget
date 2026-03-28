import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/client';
import type { Product } from '../types';
import ProductCard from '../components/ProductCard';
import LoadingSpinner from '../components/LoadingSpinner';

export default function HomePage() {
  const [featured, setFeatured] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiClient
      .get<Product[]>('/api/products')
      .then((res) => setFeatured(res.data.slice(0, 4)))
      .catch(() => setFeatured([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <main>
      <section className="hero-section py-5 text-center">
        <div className="container">
          <h1 className="display-4 fw-bold mb-3">Welcome to NextGadget</h1>
          <p className="lead mb-4">Discover the latest tech at unbeatable prices.</p>
          <Link to="/products" className="btn btn-dark btn-lg">
            Shop Now
          </Link>
        </div>
      </section>

      <section className="container py-5">
        <h2 className="mb-4">Featured Products</h2>
        {loading ? (
          <LoadingSpinner />
        ) : (
          <div className="row">
            {featured.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        )}
      </section>
    </main>
  );
}
