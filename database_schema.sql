-- ========================================
-- NutriCheck Database Schema
-- ========================================

-- 사용자 정보 테이블
CREATE TABLE User_info (
    user_id NUMBER PRIMARY KEY,
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    age NUMBER,
    gender CHAR(1) CHECK (gender IN ('M', 'F')),  -- Male/Female
    height NUMBER,
    weight NUMBER,
    goal_weight NUMBER,  -- 목표 체중 추가
    goal_type VARCHAR(20) CHECK (goal_type IN ('DIET', 'GAIN', 'MAINTAIN')),  -- 다이어트/증량/유지
    activity_level VARCHAR(20),
    bmr NUMBER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 식품 정보 테이블 (영양소 정보 확장)
CREATE TABLE Food (
    food_id NUMBER PRIMARY KEY,
    food_name VARCHAR(100) NOT NULL,
    serving_size NUMBER DEFAULT 100,  -- 기준량 (g)
    serving_unit VARCHAR(20) DEFAULT 'g',  -- 단위

    -- 기본 영양소
    calories NUMBER(8,2) DEFAULT 0,
    carbohydrate NUMBER(8,2) DEFAULT 0,
    protein NUMBER(8,2) DEFAULT 0,
    fat NUMBER(8,2) DEFAULT 0,

    -- 세부 영양소 (UI 분석 화면에 표시되는 항목들)
    saturated_fat NUMBER(8,2) DEFAULT 0,    -- 포화지방
    trans_fat NUMBER(8,2) DEFAULT 0,        -- 트랜스지방
    unsaturated_fat NUMBER(8,2) DEFAULT 0,  -- 불포화지방
    sugar NUMBER(8,2) DEFAULT 0,            -- 당류
    sodium NUMBER(8,2) DEFAULT 0,           -- 나트륨
    cholesterol NUMBER(8,2) DEFAULT 0,      -- 콜레스테롤
    calcium NUMBER(8,2) DEFAULT 0,          -- 칼슘

    -- 추가 영양소 (향후 확장 가능)
    fiber NUMBER(8,2) DEFAULT 0,            -- 식이섬유
    vitamin_a NUMBER(8,2) DEFAULT 0,
    vitamin_c NUMBER(8,2) DEFAULT 0,
    iron NUMBER(8,2) DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 식사 시간대 구분 테이블 (아침/점심/저녁/간식)
CREATE TABLE Meal_type (
    meal_type_id NUMBER PRIMARY KEY,
    meal_name VARCHAR(20) NOT NULL UNIQUE,  -- BREAKFAST, LUNCH, DINNER, SNACK
    display_name VARCHAR(20) NOT NULL       -- 아침, 점심, 저녁, 간식
);

-- 초기 데이터 삽입
INSERT INTO Meal_type VALUES (1, 'BREAKFAST', '아침');
INSERT INTO Meal_type VALUES (2, 'LUNCH', '점심');
INSERT INTO Meal_type VALUES (3, 'DINNER', '저녁');
INSERT INTO Meal_type VALUES (4, 'SNACK', '간식');

-- 식품 섭취 기록 테이블 (개선)
CREATE TABLE Food_intake (
    intake_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    food_id NUMBER NOT NULL,
    meal_type_id NUMBER NOT NULL,           -- 식사 시간대 추가
    intake_date DATE DEFAULT SYSDATE,       -- 날짜
    intake_time TIMESTAMP DEFAULT SYSTIMESTAMP,  -- 정확한 시간
    intake_amount NUMBER(8,2) CHECK (intake_amount > 0),  -- 섭취량 (g)

    FOREIGN KEY (user_id) REFERENCES User_info(user_id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES Food(food_id),
    FOREIGN KEY (meal_type_id) REFERENCES Meal_type(meal_type_id)
);

-- 인덱스 추가 (성능 최적화)
CREATE INDEX idx_intake_user_date ON Food_intake(user_id, intake_date);
CREATE INDEX idx_intake_date ON Food_intake(intake_date);

-- 연령대/성별별 권장 영양소 섭취량 테이블 (성별 추가)
CREATE TABLE Required_nutrients (
    nutrient_id NUMBER PRIMARY KEY,
    age_range VARCHAR(10),  -- '20-29', '30-39', etc.
    gender CHAR(1) CHECK (gender IN ('M', 'F')),

    -- 권장 섭취량
    req_calories NUMBER(8,2),
    req_carbohydrate NUMBER(8,2),
    req_protein NUMBER(8,2),
    req_fat NUMBER(8,2),
    req_saturated_fat NUMBER(8,2),
    req_trans_fat NUMBER(8,2),
    req_sugar NUMBER(8,2),
    req_sodium NUMBER(8,2),
    req_cholesterol NUMBER(8,2),
    req_calcium NUMBER(8,2),
    req_fiber NUMBER(8,2),

    UNIQUE(age_range, gender)
);

-- 일일 영양 분석 결과 테이블 (개선)
CREATE TABLE Daily_analysis (
    analysis_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    analysis_date DATE NOT NULL,

    -- 실제 섭취량 (계산된 값)
    total_calories NUMBER(8,2),
    total_carbohydrate NUMBER(8,2),
    total_protein NUMBER(8,2),
    total_fat NUMBER(8,2),
    total_sodium NUMBER(8,2),
    total_cholesterol NUMBER(8,2),
    total_calcium NUMBER(8,2),

    -- 권장 섭취량 대비 상태 (OVER: 초과, NORMAL: 적정, UNDER: 부족)
    calories_status VARCHAR(10) CHECK (calories_status IN ('OVER', 'NORMAL', 'UNDER')),
    carbohydrate_status VARCHAR(10) CHECK (carbohydrate_status IN ('OVER', 'NORMAL', 'UNDER')),
    protein_status VARCHAR(10) CHECK (protein_status IN ('OVER', 'NORMAL', 'UNDER')),
    fat_status VARCHAR(10) CHECK (fat_status IN ('OVER', 'NORMAL', 'UNDER')),
    sodium_status VARCHAR(10) CHECK (sodium_status IN ('OVER', 'NORMAL', 'UNDER')),
    cholesterol_status VARCHAR(10) CHECK (cholesterol_status IN ('OVER', 'NORMAL', 'UNDER')),
    calcium_status VARCHAR(10) CHECK (calcium_status IN ('OVER', 'NORMAL', 'UNDER')),

    -- AI 분석 결과 (충분한 공간 확보)
    ai_summary TEXT,  -- 종합 분석
    ai_recommendations TEXT,  -- 개선 제안

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES User_info(user_id) ON DELETE CASCADE,
    UNIQUE(user_id, analysis_date)  -- 하루에 하나의 분석만
);

-- AI 추천 주간 식단표 테이블 (새로 추가)
CREATE TABLE Weekly_meal_plan (
    plan_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    week_start_date DATE NOT NULL,  -- 주의 시작일 (월요일)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES User_info(user_id) ON DELETE CASCADE
);

-- 추천 식단 상세 테이블
CREATE TABLE Meal_plan_detail (
    detail_id NUMBER PRIMARY KEY,
    plan_id NUMBER NOT NULL,
    day_of_week NUMBER CHECK (day_of_week BETWEEN 1 AND 7),  -- 1=월요일, 7=일요일
    meal_type_id NUMBER NOT NULL,
    food_id NUMBER NOT NULL,
    recommended_amount NUMBER(8,2),  -- 추천 섭취량
    estimated_calories NUMBER(8,2),  -- 예상 칼로리

    FOREIGN KEY (plan_id) REFERENCES Weekly_meal_plan(plan_id) ON DELETE CASCADE,
    FOREIGN KEY (meal_type_id) REFERENCES Meal_type(meal_type_id),
    FOREIGN KEY (food_id) REFERENCES Food(food_id)
);

-- 사용자 알림/피드백 테이블 (선택적)
CREATE TABLE User_notification (
    notification_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    notification_type VARCHAR(20),  -- ANALYSIS, RECOMMENDATION, GOAL_ACHIEVED
    message TEXT,
    is_read CHAR(1) DEFAULT 'N' CHECK (is_read IN ('Y', 'N')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES User_info(user_id) ON DELETE CASCADE
);

-- 시퀀스 생성 (Oracle의 경우)
CREATE SEQUENCE seq_user_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_food_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_intake_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_nutrient_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_analysis_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_plan_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_detail_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_notification_id START WITH 1 INCREMENT BY 1;

-- ========================================
-- 샘플 데이터 (테스트용)
-- ========================================

-- 샘플 식품 데이터
INSERT INTO Food VALUES (
    1, '백미밥', 100, 'g',
    130, 28.7, 2.5, 0.3,
    0.1, 0, 0.2, 0, 5, 0, 3,
    0.3, 0, 0, 0.3,
    CURRENT_TIMESTAMP
);

INSERT INTO Food VALUES (
    2, '김치찌개', 100, 'g',
    50, 5.2, 4.1, 2.8,
    0.8, 0, 1.5, 1.2, 450, 15, 40,
    1.0, 100, 10, 0.8,
    CURRENT_TIMESTAMP
);

-- 권장 영양소 샘플 (20대 남성)
INSERT INTO Required_nutrients VALUES (
    1, '20-29', 'M',
    2500, 325, 65, 55, 15, 2, 50, 2000, 300, 800, 25
);

-- 권장 영양소 샘플 (20대 여성)
INSERT INTO Required_nutrients VALUES (
    2, '20-29', 'F',
    2000, 260, 55, 45, 12, 2, 40, 1500, 300, 800, 20
);

COMMIT;