CREATE TABLE PRODUCT
(
	id CHAR(6) NOT NULL,
	name NVARCHAR(1000) NOT NULL,
	amount INT,
	urlimg VARCHAR(1000) DEFAULT '#',
	barcode NCHAR(20) NULL,
	
)

INSERT INTO PRODUCT
VALUES ('11111',N'Kem trị nám tàn nhang White Skin Melasma Hương Thảo Group 35g',10,
'Kem-tri-nam-tan-nhang-White-Skin-Melasma-Huong-Thao-Group-35g-2318.jpg',
'8938511751338'),
('22222',N'Men sống cho tiêu hóa Hồng Yến Cầu Bình An',2,
'Men-song-cho-tieu-hoa-Hong-Yen-Cau-Binh-An-2325.jpg',
'8938511751331'),
('33333',N'Serum trị mụn bọc, ẩn dưới da, mủ, cám, đầu đen Bà Lão cho da thường – Tái Tạo Hỏa Tốc 30ml',5,
'Serum-tri-mun-Ba-Lao-cho-da-thuong-Tai-Tao-Hoa-Toc-30ml-2180.jpg',
'8938511751332')



-- set primary key
ALTER TABLE PRODUCT
ADD CONSTRAINT product_pk PRIMARY KEY (id);

 Select * from PRODUCT
