package com.example.community.domain.repository;

import com.example.community.domain.entity.Croom;
import com.example.groupbuying.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CroomRepository extends JpaRepository<Croom, Integer> {
    @Query("SELECT c FROM Croom c WHERE " +
            "(:roomTitle IS NULL OR :roomTitle = '' OR c.roomTitle LIKE CONCAT('%', :roomTitle, '%')) AND " +
            "(:communityCategory IS NULL OR :communityCategory = '' OR c.communityCategory = :communityCategory) AND " +
            "(:region IS NULL OR :region = '' OR c.region = :region)")
    List<Croom> findBySearchCriteria(@Param("roomTitle") String roomTitle,
                                     @Param("communityCategory") String communityCategory,
                                     @Param("region") String region);

    @Query("SELECT c FROM Croom c WHERE " +
            "(:roomTitle IS NULL OR :roomTitle = '' OR c.roomTitle LIKE CONCAT('%', :roomTitle, '%')) AND " +
            "(:detailCategory IS NULL OR :detailCategory = '' OR c.detailCategory = :detailCategory) AND " +
            "(:detailRegion IS NULL OR :detailRegion = '' OR c.detailRegion = :detailRegion)")
    List<Croom> findByDetailCriteria(@Param("roomTitle") String roomTitle,
                                               @Param("detailCategory") String detailCategory,
                                               @Param("detailRegion") String detailRegion);



    // 방 이름으로 검색하는 메소드 추가
    List<Croom> findByRoomTitle(String roomTitle);

    // 카테고리로 필터링하는 메소드 추가
    List<Croom> findByCommunityCategory(String communityCategory);

    // 지역으로 필터링하는 메소드 추가
    List<Croom> findByRegion(String region);



    // 카테고리로 필터링하는 메소드 추가
    List<Croom> findByDetailCategory(String detailCategory);

    // 지역으로 필터링하는 메소드 추가
    List<Croom> findByDetailRegion(String detailRegion);


}
