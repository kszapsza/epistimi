import './NotFound.scss';

export const NotFound = (): JSX.Element => {
  return (
    <div className={"not-found"}>
      <h1 className={"not-found-code"}>
        404
      </h1>
      <div className={"not-found-desc"}>
        Strona o&nbsp;podanym adresie nie istnieje
      </div>
    </div>
  );
};
