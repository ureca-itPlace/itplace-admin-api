package com.itplace.adminapi.favorite.repository;

import com.itplace.adminapi.benefit.dto.FavoriteRankProjection;
import com.itplace.adminapi.favorite.entity.Favorite;
import com.itplace.adminapi.log.dto.RankResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query(value = """
            SELECT f.benefitId AS benefitId,
            p.partnerName AS partnerName,
            b.mainCategory AS mainCategory,
            COUNT(*) AS favoriteCount,
            ROW_NUMBER() OVER (ORDER BY COUNT(*) DESC) AS favoriteRank
            FROM favorite f
            JOIN benefit b ON f.benefitId = b.benefitId
            JOIN partner p ON b.partnerId = p.partnerId
            GROUP BY f.benefitId
            ORDER BY COUNT(f.userId) DESC
            LIMIT :limit;
        """, nativeQuery = true)
    List<FavoriteRankProjection> findTopFavoriteRank(@Param("limit") int limit);

    @Query(value = """
            SELECT f.benefitId AS Id, COUNT(*) AS count
            FROM favorite f
            JOIN benefit b ON f.benefitId = b.benefitId
            WHERE b.mainCategory = '기본 혜택'
            GROUP BY f.benefitId
            ORDER BY count desc
        """, nativeQuery = true)
    List<RankResult> findBasicFavoriteRank();

    @Query(value = """
                SELECT f.benefitId AS Id, COUNT(*) AS count
                FROM favorite f
                JOIN benefit b ON f.benefitId = b.benefitId
                WHERE b.mainCategory = 'VIP 콕'
                GROUP BY f.benefitId
                ORDER BY count desc
            """, nativeQuery = true)
    List<RankResult> findVipFavoriteRank();
}
