import type { Order } from '../types';

interface OrderSummaryProps {
  order: Order;
}

export default function OrderSummary({ order }: OrderSummaryProps) {
  return (
    <div className="card mb-3">
      <div className="card-header d-flex justify-content-between align-items-center">
        <span className="fw-bold">Order #{order.id}</span>
        <span className={`badge ${order.status === 'COMPLETED' ? 'bg-success' : 'bg-warning text-dark'}`}>
          {order.status}
        </span>
      </div>
      <div className="card-body">
        <p className="text-muted small mb-2">
          Placed on {new Date(order.createdAt).toLocaleDateString()}
        </p>
        <table className="table table-sm mb-2">
          <thead>
            <tr>
              <th>Product ID</th>
              <th>Qty</th>
              <th>Unit Price</th>
            </tr>
          </thead>
          <tbody>
            {order.items.map((item) => (
              <tr key={item.id}>
                <td>{item.productId}</td>
                <td>{item.quantity}</td>
                <td>${item.unitPrice.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <p className="fw-bold text-end mb-0">Total: ${order.total.toFixed(2)}</p>
      </div>
    </div>
  );
}
