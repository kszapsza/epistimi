import './Footer.scss';

export const Footer = (): JSX.Element => {
  return (
    <footer className="footer">
      <div className="footer-logo">
        Epistimi
      </div>
      <div className="footer-columns">
        <div className="footer-column">
          <div className="footer-column-header">Firma</div>
          <div className="footer-column-item">O nas</div>
          <div className="footer-column-item">Kariera</div>
          <div className="footer-column-item">Kontakt</div>
        </div>
        <div className="footer-column">
          <div className="footer-column-header">Oferta</div>
          <div className="footer-column-item">Dla szkół</div>
          <div className="footer-column-item">Dla samorządów</div>
        </div>
        <div className="footer-column">
          <div className="footer-column-header">Dla klienta</div>
          <div className="footer-column-item">Pomoc techniczna</div>
          <div className="footer-column-item">Zgłoś błąd</div>
          <div className="footer-column-item">Regulamin</div>
          <div className="footer-column-item">Poltyka prywatności</div>
        </div>
      </div>
      <div className="footer-copyright">
        &#169; 2022 Epistimi Sp. z&nbsp;o.o.
      </div>
    </footer>
  );
};
