
CREATE TABLE ACCOUNT
(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	username [varchar](255) NOT NULL,
	password [varchar](255) NULL,
)

Select * from account

drop table ACCOUNT

delete From account where id = 1