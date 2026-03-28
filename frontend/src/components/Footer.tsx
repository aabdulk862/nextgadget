export default function Footer() {
  return (
    <footer className="bg-dark text-light py-4 mt-auto">
      <div className="container text-center">
        <p className="mb-1">&copy; {new Date().getFullYear()} NextGadget. All rights reserved.</p>
        <p className="small text-muted mb-0">Your one-stop shop for the latest tech.</p>
      </div>
    </footer>
  );
}
