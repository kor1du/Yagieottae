--USER DATA

INSERT
    INTO user (address,address_detail,nickname, password,phone,profile_img_path,reg_date,role,user_id)
    VALUES (
            '경기 성남시 분당구 대왕판교로 477',
            '1층 101호',
            '유저',
            '$2a$10$lY5kqc6/cN.AaJNp15zI3etf75hCajlkS5kv0rFKuBXIT6kPbA81S',
            '010-1234-1234',
            'https://yagieottae-s3-bucket.s3.ap-northeast-2.amazonaws.com/MemberDefaultProfileImg.png',
            NOW(),
            'USER',
            'user'
            );

INSERT
    INTO user (address,address_detail,nickname, password,phone,profile_img_path,reg_date,role,user_id)
    VALUES (
            '경기 성남시 분당구 대왕판교로 477',
            '1층 101호',
            '관리자',
            '$2a$10$lY5kqc6/cN.AaJNp15zI3etf75hCajlkS5kv0rFKuBXIT6kPbA81S',
            '010-0000-0000',
            'https://yagieottae-s3-bucket.s3.ap-northeast-2.amazonaws.com/MemberDefaultProfileImg.png',
            NOW(),
            'ADMIN',
            'admin'
            );

INSERT
    INTO user (address,address_detail,nickname, password,phone,profile_img_path,reg_date,role,user_id)
    VALUES (
            '테스트용 주소',
            '1층 101호',
            '권한없는 유저',
            '$2a$10$lY5kqc6/cN.AaJNp15zI3etf75hCajlkS5kv0rFKuBXIT6kPbA81S',
            '010-1234-5678',
            'https://yagieottae-s3-bucket.s3.ap-northeast-2.amazonaws.com/MemberDefaultProfileImg.png',
            NOW(),
            'USER',
            'userWithoutPermission'
            );

--PILL DATA

INSERT
    INTO pill (item_seq, item_name, image_path, entp_name, main_ingredient, efcy_qesitm, use_method_qesitm, atpn_warn_qesitm, atpn_qesitm, intrc_qesitm, se_qesitm, deposit_method_qesitm, reg_date, edit_date)
    VALUES (
            '199400202',
            '판피린티정',
            'https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/146031134011300035\r',
            '동아제약(주)',
            '아세트아미노펜,클로르페니라민말레산염,카페인무수물',
            '이약은감기의여러증상(콧물,코막힘,재채기,인후통,오한,발열,관절통,두통,근육통)의완화에사용합니다.',
            '성인은1회1정씩,1일3회식후30분에복용합니다.',
            '매일세잔이상정기적음주자가이약또는다른해열진통제를복용할때는의사또는약사와상의하십시오.간손상을일으킬수있습니다.',
            '이약에과민증환자및경험자,다른해열진통제,감기약복용시천식경험자,만3개월미만의영아,MAO억제제(항우울제,항정신병제,감정조절제,항파킨슨제등)를복용하고있거나복용을중단한후2주이내의사람은 이약을복용하지마십시오.',
            '아세트아미노펜을포함하는다른제품,MAO억제제(항우울제,항정신병제,감정조절제,항파킨슨제등),진해(기침을그치게함)거담제(가래약),다른감기약,해열진통제,진정제,항히스타민제를함유하는내복약(비염용경구제,멀미약,알레르기용약)과함께복용하지마십시오.',
            '발진·발적(충혈되어붉어짐),가려움,구역·구토,식욕부진,변비,부기,배뇨(소변을눔)곤란,목마름(지속적이거나심한),어지러움이나타나는경우복용을즉각중지하고의사또는약사와상의하십시오.',
            '습기와빛을피해실온에서보관하십시오.어린이의손이닿지않는곳에보관하십시오.',
            '2021-01-29 00:00:00',
            '2021-12-27 00:00:00'
            );

INSERT
    INTO pill (item_seq, item_name, image_path, entp_name, main_ingredient, efcy_qesitm, use_method_qesitm, atpn_warn_qesitm, atpn_qesitm, intrc_qesitm, se_qesitm, deposit_method_qesitm, reg_date, edit_date)
    VALUES (
            '199400203',
            '베아제정',
            'https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/146031134011300035\r',
            '동아제약(주)',
            '아세트아미노펜,클로르페니라민말레산염,카페인무수물',
            '이약은감기의여러증상(콧물,코막힘,재채기,인후통,오한,발열,관절통,두통,근육통)의완화에사용합니다.',
            '성인은1회1정씩,1일3회식후30분에복용합니다.',
            '매일세잔이상정기적음주자가이약또는다른해열진통제를복용할때는의사또는약사와상의하십시오.간손상을일으킬수있습니다.',
            '이약에과민증환자및경험자,다른해열진통제,감기약복용시천식경험자,만3개월미만의영아,MAO억제제(항우울제,항정신병제,감정조절제,항파킨슨제등)를복용하고있거나복용을중단한후2주이내의사람은 이약을복용하지마십시오.',
            '아세트아미노펜을포함하는다른제품,MAO억제제(항우울제,항정신병제,감정조절제,항파킨슨제등),진해(기침을그치게함)거담제(가래약),다른감기약,해열진통제,진정제,항히스타민제를함유하는내복약(비염용경구제,멀미약,알레르기용약)과함께복용하지마십시오.',
            '발진·발적(충혈되어붉어짐),가려움,구역·구토,식욕부진,변비,부기,배뇨(소변을눔)곤란,목마름(지속적이거나심한),어지러움이나타나는경우복용을즉각중지하고의사또는약사와상의하십시오.',
            '습기와빛을피해실온에서보관하십시오.어린이의손이닿지않는곳에보관하십시오.',
            '2021-01-29 00:00:00',
            '2021-12-27 00:00:00'
            );

INSERT
    INTO pill (item_seq, item_name, image_path, entp_name, main_ingredient, efcy_qesitm, use_method_qesitm, atpn_warn_qesitm, atpn_qesitm, intrc_qesitm, se_qesitm, deposit_method_qesitm, reg_date, edit_date)
    VALUES (
            '199400203',
            '베아제정2',
            'https://nedrug.mfds.go.kr/pbp/cmn/itemImageDownload/146031134011300035\r',
            '동아제약(주)',
            '아세트아미노펜,클로르페니라민말레산염,카페인무수물',
            '이약은감기의여러증상(콧물,코막힘,재채기,인후통,오한,발열,관절통,두통,근육통)의완화에사용합니다.',
            '성인은1회1정씩,1일3회식후30분에복용합니다.',
            '매일세잔이상정기적음주자가이약또는다른해열진통제를복용할때는의사또는약사와상의하십시오.간손상을일으킬수있습니다.',
            '이약에과민증환자및경험자,다른해열진통제,감기약복용시천식경험자,만3개월미만의영아,MAO억제제(항우울제,항정신병제,감정조절제,항파킨슨제등)를복용하고있거나복용을중단한후2주이내의사람은 이약을복용하지마십시오.',
            '아세트아미노펜을포함하는다른제품,MAO억제제(항우울제,항정신병제,감정조절제,항파킨슨제등),진해(기침을그치게함)거담제(가래약),다른감기약,해열진통제,진정제,항히스타민제를함유하는내복약(비염용경구제,멀미약,알레르기용약)과함께복용하지마십시오.',
            '발진·발적(충혈되어붉어짐),가려움,구역·구토,식욕부진,변비,부기,배뇨(소변을눔)곤란,목마름(지속적이거나심한),어지러움이나타나는경우복용을즉각중지하고의사또는약사와상의하십시오.',
            '습기와빛을피해실온에서보관하십시오.어린이의손이닿지않는곳에보관하십시오.',
            '2021-01-29 00:00:00',
            '2021-12-27 00:00:00'
            );

--ALRAM DATA

--ALLDAY
INSERT
    INTO alram (alram_time,before_meal,days,dosing_time,edit_date,reg_date,user_id,pill_id)
    VALUES (
            '06:30',
            1,
            '1,2,3,4,5,6,7',
            30L,
            NOW(),
            NOW(),
            1L,
            1L
            );

--REVIEW DATA
INSERT
    INTO review (rate,review_message,user_id,pill_id,reg_date,edit_date)
    VALUES (
            1,
            '리뷰 메시지1',
            1L,
            1L,
            '2023-01-01 12:34:00',
            NOW()
            );

INSERT
    INTO review (rate,review_message,user_id,pill_id,reg_date,edit_date)
    VALUES (
            1,
            '리뷰 메시지2',
            1L,
            1L,
            NOW(),
            NOW()
            );

INSERT
    INTO review (rate,review_message,user_id,pill_id,reg_date,edit_date)
    VALUES (
            1,
            '리뷰 메시지3',
            1L,
            1L,
            NOW(),
            NOW()
            );