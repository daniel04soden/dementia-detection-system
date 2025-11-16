import React from "react";
import styles from "./newscards.module.css";

interface News {
  headline: string;
  snippet: string;
  url: string;
}

const NewsCards: React.FC<{ news: News }> = ({ news }) => {
  return (
    <div className={styles.newsCard}>
      <h3>{news.headline}</h3>
      <p>{news.snippet}</p>
      <a href={news.url} target="_blank">
        Link here
      </a>
    </div>
  );
};

export default NewsCards;