USE [master]
GO
/****** Object:  Database [nguyentrilong]    Script Date: 22/05/2021 9:10:58 AM ******/
CREATE DATABASE [nguyentrilong] ON  PRIMARY 
( NAME = N'nguyentrilong', FILENAME = N'D:\HostingTVNet\TVNetGroup\NguyenTriLong\nguyentrilong.mdf' , SIZE = 3072KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'nguyentrilong_log', FILENAME = N'D:\HostingTVNet\TVNetGroup\NguyenTriLong\nguyentrilong_log.ldf' , SIZE = 29696KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [nguyentrilong] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [nguyentrilong].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [nguyentrilong] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [nguyentrilong] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [nguyentrilong] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [nguyentrilong] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [nguyentrilong] SET ARITHABORT OFF 
GO
ALTER DATABASE [nguyentrilong] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [nguyentrilong] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [nguyentrilong] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [nguyentrilong] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [nguyentrilong] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [nguyentrilong] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [nguyentrilong] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [nguyentrilong] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [nguyentrilong] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [nguyentrilong] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [nguyentrilong] SET  DISABLE_BROKER 
GO
ALTER DATABASE [nguyentrilong] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [nguyentrilong] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [nguyentrilong] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [nguyentrilong] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [nguyentrilong] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [nguyentrilong] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [nguyentrilong] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [nguyentrilong] SET RECOVERY FULL 
GO
ALTER DATABASE [nguyentrilong] SET  MULTI_USER 
GO
ALTER DATABASE [nguyentrilong] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [nguyentrilong] SET DB_CHAINING OFF 
GO
EXEC sys.sp_db_vardecimal_storage_format N'nguyentrilong', N'ON'
GO
USE [nguyentrilong]
GO
/****** Object:  User [nguyentrilong]    Script Date: 22/05/2021 9:10:58 AM ******/
CREATE USER [nguyentrilong] FOR LOGIN [nguyentrilong] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [nguyentrilong]
GO
/****** Object:  Table [dbo].[PRODUCT]    Script Date: 22/05/2021 9:10:58 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PRODUCT](
	[id] [char](6) NOT NULL,
	[name] [nvarchar](1000) NOT NULL,
	[amount] [int] NULL,
	[urlimg] [varchar](1000) NULL DEFAULT ('#'),
	[barcode] [nchar](20) NULL,
 CONSTRAINT [product_pk] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
USE [master]
GO
ALTER DATABASE [nguyentrilong] SET  READ_WRITE 
GO
