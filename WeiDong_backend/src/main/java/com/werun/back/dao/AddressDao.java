package com.werun.back.dao;

import com.werun.back.entity.AddressEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface AddressDao {

//    @Select("select * from receiver where M_id=#{M_id} order by acquiescence desc")
//    List<AddressEntity> getAllAddressByMid(@Param("M_id") String M_id);

    //通过Uid获取默认地址
    @Select("select " +
            "ad_id,ad_uid,ad_name,ad_phone,ad_province,ad_city,ad_county,ad_dtl,ad_default " +
            "from address " +
            "where ad_uid=#{1} and ad_status=#{2} order by ad_default desc limit 0,1;")
    AddressEntity getDefaultAdd(@Param("1") String uid,@Param("2")int status);
    //通过Uid  get add list
    @Select("select " +
            "ad_id,ad_uid,ad_name,ad_phone,ad_province,ad_city,ad_county,ad_dtl,ad_default " +
            "from address " +
            "where ad_uid=#{1} and ad_status=#{2} order by ad_default desc;")
    List<AddressEntity> getAddList(@Param("1") String uid,@Param("2")int status);

    //通过aid获取add
    @Select("select " +
            "ad_id,ad_uid,ad_name,ad_phone,ad_province,ad_city,ad_county,ad_dtl,ad_default " +
            "from address " +
            "where ad_id=#{1};")
    AddressEntity getByAid(@Param("1") String aid);

    //获取个人有效地址数
    @Select("select count(*) " +
            "from address " +
            "where ad_uid=#{1} and ad_status=#{2} ;")
    int getCount(@Param("1")String uid,@Param("2")int status);


//    @Select("select count(*) from receiver where M_id=#{M_id}")
//    int getAddressCountByMid(@Param("M_id") String M_id);
//
//    @Select("select * from receiver where M_id=#{M_id} order by acquiescence desc limit 1")
//    AddressEntity getDefaultAddress(@Param("M_id") String M_id);
//
//    @Delete("delete from receiver where Re_id =#{Re_id}")
//    void deleteAddByReid(@Param("Re_id") String Re_id);
//
//    @Update("update receiver set acquiescence='是' where Re_id=#{Re_id}")
//    void setDefaultAdd(@Param("Re_id") String Re_id);

    //添加地址
    @Insert("insert into address" +
            "(ad_id,ad_uid,ad_name,ad_phone,ad_province,ad_city,ad_county,ad_dtl,ad_default)" +
            " values" +
            "(#{adId},#{adUid},#{adName},#{adPhone},#{adProvince},#{adCity},#{adCounty},#{adDtl},#{adDefault})")
    void insert(AddressEntity addressEntity);

//
//    @Update("update receiver set acquiescence='否' where M_id=#{M_id}")
//    void setAllNotDefaust(@Param("M_id") String M_id);
//
//    @Update("update receiver set name=#{name},phone=#{phone},acquiescence=#{acquiescence}," +
//            "province=#{province},city=#{city},county=#{county},address_details=#{address_details},M_id=#{M_id}" +
//            " where Re_id=#{Re_id}")
//    void updateAddress(AddressEntity addressEntity);
//

    //set all undefault
    @Update("update address " +
            "set ad_default=0 " +
            "where ad_uid=#{1} and ad_status=#{2};")
    int setUndefault(@Param("1")String uid,@Param("2")int status);
    //set default
    @Update("update address " +
            "set ad_default=1 " +
            "where ad_id=#{1};")
    int setDefault(@Param("1")String aid);

    //delete add
    @Update("update address " +
            "set ad_status=2 " +
            "where ad_id=#{1};")
    int delete(@Param("1")String aid);
}
