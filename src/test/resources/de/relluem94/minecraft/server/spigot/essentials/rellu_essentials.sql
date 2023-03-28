USE "rellu_essentials";

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

-- --------------------------------------------------------

--
-- Table structure for table `bag`
--

CREATE TABLE `bag` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `player_fk` int NOT NULL,
  `bag_type_fk` int NOT NULL,
  `slot_1_value` bigint NOT NULL DEFAULT '0',
  `slot_2_value` bigint NOT NULL DEFAULT '0',
  `slot_3_value` bigint NOT NULL DEFAULT '0',
  `slot_4_value` bigint NOT NULL DEFAULT '0',
  `slot_5_value` bigint NOT NULL DEFAULT '0',
  `slot_6_value` bigint NOT NULL DEFAULT '0',
  `slot_7_value` bigint NOT NULL DEFAULT '0',
  `slot_8_value` bigint NOT NULL DEFAULT '0',
  `slot_9_value` bigint NOT NULL DEFAULT '0',
  `slot_10_value` bigint NOT NULL DEFAULT '0',
  `slot_11_value` bigint NOT NULL DEFAULT '0',
  `slot_12_value` bigint NOT NULL DEFAULT '0',
  `slot_13_value` bigint NOT NULL DEFAULT '0',
  `slot_14_value` bigint NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `bag`
--

INSERT INTO `bag` (`id`, `created`, `createdby`, `updated`, `updatedby`, `deleted`, `deletedby`, `player_fk`, `bag_type_fk`, `slot_1_value`, `slot_2_value`, `slot_3_value`, `slot_4_value`, `slot_5_value`, `slot_6_value`, `slot_7_value`, `slot_8_value`, `slot_9_value`, `slot_10_value`, `slot_11_value`, `slot_12_value`, `slot_13_value`, `slot_14_value`) VALUES
(1, '2023-02-07 14:00:10', 2, '2023-03-18 12:45:26', 2, NULL, NULL, 2, 1, 1180, 61, 78, 58, 114, 352, 0, 157, 219559, 168707774, 9, 132, 284, 1061043),
(2, '2023-02-07 16:33:56', 2, '2023-03-19 18:02:33', 2, NULL, NULL, 2, 2, 79, 74, 2926, 323, 76, 85, 0, 147, 1, 0, 272, 149, 198, 213),
(3, '2023-03-06 19:56:49', 2, '2023-03-12 16:44:07', 2, NULL, NULL, 2, 3, 499, 448, 260, 1, 2, 35, 0, 0, 0, 1, 0, 0, 0, 0),
(4, '2023-03-06 20:17:16', 2, '2023-03-12 12:14:35', 2, NULL, NULL, 2, 4, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
(5, '2023-03-06 20:21:33', 2, '2023-03-12 16:36:07', 2, NULL, NULL, 2, 5, 266, 21, 0, 0, 0, 0, 0, 248, 32, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `bag_type`
--

CREATE TABLE `bag_type` (
  `id` int NOT NULL,
  `displayname` varchar(94) COLLATE utf8mb3_bin NOT NULL,
  `name` varchar(94) COLLATE utf8mb3_bin NOT NULL,
  `cost` int NOT NULL,
  `slot_1_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_2_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_3_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_4_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_5_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_6_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_7_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_8_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_9_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_10_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_11_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_12_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_13_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_14_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `bag_type`
--

INSERT INTO `bag_type` (`id`, `displayname`, `name`, `cost`, `slot_1_name`, `slot_2_name`, `slot_3_name`, `slot_4_name`, `slot_5_name`, `slot_6_name`, `slot_7_name`, `slot_8_name`, `slot_9_name`, `slot_10_name`, `slot_11_name`, `slot_12_name`, `slot_13_name`, `slot_14_name`) VALUES
(1, 'Mining Bag', 'MINER', 500000, 'STONE', 'COBBLESTONE', 'GRANITE', 'DIORITE', 'ANDESITE', 'DEEPSLATE', 'COBBLED_DEEPSLATE', 'COAL', 'RAW_COPPER', 'RAW_IRON', 'RAW_GOLD', 'LAPIS_LAZULI', 'REDSTONE', 'DIAMOND'),
(2, 'Farming Bag', 'FARMER', 500000, 'WHEAT', 'POTATO', 'CARROT', 'SUGAR_CANE', 'BEETROOT', 'PUMPKIN', 'MELON_SLICE', 'WHEAT_SEEDS', 'APPLE', 'SWEET_BERRIES', 'KELP', 'BEETROOT_SEEDS', 'COCOA_BEANS', 'NETHER_WART'),
(3, 'Monster Bag', 'MONSTER', 500000, 'ARROW', 'BONE', 'ROTTEN_FLESH', 'SPIDER_EYE', 'STRING', 'GUNPOWDER', 'SLIME_BALL', 'GHAST_TEAR', 'BLAZE_ROD', 'ENDER_PEARL', 'FEATHER', 'SHULKER_SHELL', 'INK_SAC', 'GLOW_INK_SAC'),
(4, 'Shepherd Bag', 'SHEPHERD', 500000, 'BLACK_WOOL', 'BROWN_WOOL', 'RED_WOOL', 'ORANGE_WOOL', 'YELLOW_WOOL', 'LIME_WOOL', 'GREEN_WOOL', 'CYAN_WOOL', 'LIGHT_BLUE_WOOL', 'BLUE_WOOL', 'PURPLE_WOOL', 'MAGENTA_WOOL', 'PINK_WOOL', 'WHITE_WOOL'),
(5, 'Lumberjack Bag', 'LUMBERJACK', 500000, 'OAK_LOG', 'BIRCH_LOG', 'JUNGLE_LOG', 'ACACIA_LOG', 'DARK_OAK_LOG', 'SPRUCE_LOG', 'MANGROVE_LOG', 'OAK_SAPLING', 'BIRCH_SAPLING', 'JUNGLE_SAPLING', 'ACACIA_SAPLING', 'DARK_OAK_SAPLING', 'SPRUCE_SAPLING', 'MANGROVE_PROPAGULE');

-- --------------------------------------------------------

--
-- Table structure for table `bank_account`
--

CREATE TABLE `bank_account` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `player_fk` int NOT NULL,
  `value` double NOT NULL,
  `bank_tier_fk` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `bank_account`
--

INSERT INTO `bank_account` (`id`, `created`, `createdby`, `updated`, `updatedby`, `deleted`, `deletedby`, `player_fk`, `value`, `bank_tier_fk`) VALUES
(1, '2023-01-22 00:28:33', 1, '2023-03-23 09:32:49', 2, NULL, NULL, 2, 7592634992.900983, 3),
(2, '2023-02-13 20:12:38', 1, '2023-02-13 20:12:47', 12, NULL, NULL, 12, 707547, 1);

-- --------------------------------------------------------

--
-- Table structure for table `bank_tier`
--

CREATE TABLE `bank_tier` (
  `id` int NOT NULL,
  `name` varchar(94) COLLATE utf8mb3_bin NOT NULL,
  `limit` bigint NOT NULL,
  `interest` float NOT NULL,
  `cost` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `bank_tier`
--

INSERT INTO `bank_tier` (`id`, `name`, `limit`, `interest`, `cost`) VALUES
(1, 'Standard', 9000000, 0.3, 50000),
(2, 'Premium', 9000000000, 0.2, 5000000),
(3, 'Exclusive', 9000000000000, 0.1, 50000000);

-- --------------------------------------------------------

--
-- Table structure for table `bank_transaction`
--

CREATE TABLE `bank_transaction` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `bank_account_fk` int NOT NULL,
  `value` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `bank_transaction`
--

INSERT INTO `bank_transaction` (`id`, `created`, `createdby`, `updated`, `updatedby`, `deleted`, `deletedby`, `bank_account_fk`, `value`) VALUES
(49, '2023-01-22 16:10:55', 2, NULL, NULL, NULL, NULL, 1, -100),
(50, '2023-01-22 16:11:04', 2, NULL, NULL, NULL, NULL, 1, 100),
(51, '2023-01-22 16:11:13', 2, NULL, NULL, NULL, NULL, 1, -50),
(52, '2023-01-22 16:11:29', 2, NULL, NULL, NULL, NULL, 1, 50),
(53, '2023-01-22 16:11:32', 2, NULL, NULL, NULL, NULL, 1, -20),
(54, '2023-01-22 16:11:38', 2, NULL, NULL, NULL, NULL, 1, 20),
(55, '2023-01-22 16:11:40', 2, NULL, NULL, NULL, NULL, 1, -5),
(56, '2023-01-22 16:11:49', 2, NULL, NULL, NULL, NULL, 1, 5),
(57, '2023-01-22 16:19:49', 2, NULL, NULL, NULL, NULL, 1, -100),
(58, '2023-01-22 16:19:58', 2, NULL, NULL, NULL, NULL, 1, 50),
(59, '2023-01-22 16:20:03', 2, NULL, NULL, NULL, NULL, 1, -50),
(60, '2023-01-22 16:20:05', 2, NULL, NULL, NULL, NULL, 1, 100),
(61, '2023-01-22 16:20:13', 2, NULL, NULL, NULL, NULL, 1, -100),
(62, '2023-01-22 16:20:15', 2, NULL, NULL, NULL, NULL, 1, 5),
(63, '2023-01-22 16:20:23', 2, NULL, NULL, NULL, NULL, 1, -5),
(64, '2023-01-22 16:20:29', 2, NULL, NULL, NULL, NULL, 1, 20),
(65, '2023-01-22 16:20:43', 2, NULL, NULL, NULL, NULL, 1, 80),
(66, '2023-01-22 16:26:16', 2, NULL, NULL, NULL, NULL, 1, -100),
(67, '2023-01-22 16:26:19', 2, NULL, NULL, NULL, NULL, 1, 5),
(68, '2023-01-22 16:30:14', 2, NULL, NULL, NULL, NULL, 1, -5),
(69, '2023-01-22 16:30:17', 2, NULL, NULL, NULL, NULL, 1, 50),
(70, '2023-01-22 16:30:22', 2, NULL, NULL, NULL, NULL, 1, -50),
(71, '2023-01-22 16:30:27', 2, NULL, NULL, NULL, NULL, 1, 20),
(72, '2023-01-22 16:30:32', 2, NULL, NULL, NULL, NULL, 1, 80),
(73, '2023-01-22 17:09:28', 2, NULL, NULL, NULL, NULL, 1, 1970.0001220703125),
(74, '2023-01-27 22:28:10', 2, NULL, NULL, NULL, NULL, 1, 1329500),
(75, '2023-01-27 22:28:36', 2, NULL, NULL, NULL, NULL, 1, 130),
(76, '2023-01-27 22:48:54', 2, NULL, NULL, NULL, NULL, 1, 680),
(77, '2023-01-28 19:32:33', 2, NULL, NULL, NULL, NULL, 1, 1661780.125),
(78, '2023-01-28 19:46:28', 2, NULL, NULL, NULL, NULL, 1, -149708),
(79, '2023-01-28 19:46:31', 2, NULL, NULL, NULL, NULL, 1, 150513),
(80, '2023-01-28 19:49:16', 2, NULL, NULL, NULL, NULL, 1, 23180),
(81, '2023-01-28 19:55:52', 2, NULL, NULL, NULL, NULL, 1, 53784.99609375),
(82, '2023-01-28 20:00:02', 2, NULL, NULL, NULL, NULL, 1, 21020),
(83, '2023-01-29 09:40:55', 2, NULL, NULL, NULL, NULL, 1, -3092940),
(84, '2023-01-29 09:41:02', 2, NULL, NULL, NULL, NULL, 1, 3356697),
(85, '2023-01-29 09:47:51', 2, NULL, NULL, NULL, NULL, 1, 37221),
(86, '2023-01-29 09:54:18', 2, NULL, NULL, NULL, NULL, 1, -678784),
(87, '2023-01-29 09:54:26', 2, NULL, NULL, NULL, NULL, 1, 678784),
(88, '2023-01-29 09:55:39', 2, NULL, NULL, NULL, NULL, 1, -3393920),
(89, '2023-01-29 09:55:42', 2, NULL, NULL, NULL, NULL, 1, 3393920),
(90, '2023-01-29 11:48:25', 2, NULL, NULL, NULL, NULL, 1, 69451),
(91, '2023-01-30 15:55:16', 2, NULL, NULL, NULL, NULL, 1, -173168.5),
(92, '2023-01-30 15:55:20', 2, NULL, NULL, NULL, NULL, 1, 208320.5),
(93, '2023-01-30 16:07:48', 2, NULL, NULL, NULL, NULL, 1, -174926),
(94, '2023-01-30 16:07:52', 2, NULL, NULL, NULL, NULL, 1, 175191),
(95, '2023-01-30 16:10:47', 2, NULL, NULL, NULL, NULL, 1, -3498780),
(96, '2023-01-31 16:14:44', 2, NULL, NULL, NULL, NULL, 1, 3554560.25),
(97, '2023-01-31 16:30:50', 2, NULL, NULL, NULL, NULL, 1, 61281),
(98, '2023-01-31 16:40:48', 2, NULL, NULL, NULL, NULL, 1, 11936.349609375),
(99, '2023-01-31 16:41:04', 2, NULL, NULL, NULL, NULL, 1, 226790.65625),
(100, '2023-01-31 16:41:12', 2, NULL, NULL, NULL, NULL, 1, -192728.5),
(101, '2023-01-31 16:41:14', 2, NULL, NULL, NULL, NULL, 1, 9636.4248046875),
(102, '2023-01-31 16:41:25', 2, NULL, NULL, NULL, NULL, 1, 183092.078125),
(103, '2023-01-31 16:45:22', 2, NULL, NULL, NULL, NULL, 1, 1664),
(104, '2023-01-31 16:45:25', 2, NULL, NULL, NULL, NULL, 1, 332.79998779296875),
(105, '2023-01-31 16:45:36', 2, NULL, NULL, NULL, NULL, 1, 6323.2001953125),
(106, '2023-02-07 16:33:21', 2, NULL, NULL, NULL, NULL, 1, -3862880),
(107, '2023-02-07 16:33:28', 2, NULL, NULL, NULL, NULL, 1, 3873135.25),
(108, '2023-02-07 16:33:52', 2, NULL, NULL, NULL, NULL, 1, -3873139.75),
(109, '2023-02-07 16:34:08', 2, NULL, NULL, NULL, NULL, 1, 3373139.5),
(110, '2023-02-10 10:23:38', 2, NULL, NULL, NULL, NULL, 1, 0.19999998807907104),
(111, '2023-02-10 18:19:16', 2, NULL, NULL, NULL, NULL, 1, 310179),
(112, '2023-02-11 13:14:24', 2, NULL, NULL, NULL, NULL, 1, 752640),
(113, '2023-02-12 15:01:03', 2, NULL, NULL, NULL, NULL, 1, -4363259),
(114, '2023-02-12 15:16:06', 2, NULL, NULL, NULL, NULL, 1, 1766242560),
(115, '2023-02-12 15:16:11', 2, NULL, NULL, NULL, NULL, 1, 883121280),
(116, '2023-02-12 15:16:15', 2, NULL, NULL, NULL, NULL, 1, 441560640),
(117, '2023-02-12 15:16:40', 2, NULL, NULL, NULL, NULL, 1, 156009232),
(118, '2023-02-12 15:16:45', 2, NULL, NULL, NULL, NULL, 1, 78004616),
(119, '2023-02-12 15:27:59', 2, NULL, NULL, NULL, NULL, 1, 39002308),
(120, '2023-02-12 15:28:05', 2, NULL, NULL, NULL, NULL, 1, 19501154),
(121, '2023-02-12 15:29:46', 2, NULL, NULL, NULL, NULL, 1, 9750577),
(122, '2023-02-12 15:29:53', 2, NULL, NULL, NULL, NULL, 1, 4875288.5),
(123, '2023-02-12 15:29:57', 2, NULL, NULL, NULL, NULL, 1, 2437644.25),
(124, '2023-02-12 15:29:59', 2, NULL, NULL, NULL, NULL, 1, 1218822.125),
(125, '2023-02-12 15:30:01', 2, NULL, NULL, NULL, NULL, 1, 609411.0625),
(126, '2023-02-12 15:30:04', 2, NULL, NULL, NULL, NULL, 1, 304705.53125),
(127, '2023-02-12 15:30:07', 2, NULL, NULL, NULL, NULL, 1, 152352.765625),
(128, '2023-02-12 15:30:09', 2, NULL, NULL, NULL, NULL, 1, 76176.3828125),
(129, '2023-02-12 15:30:12', 2, NULL, NULL, NULL, NULL, 1, 38088.19140625),
(130, '2023-02-12 15:32:35', 2, NULL, NULL, NULL, NULL, 1, 1904.4),
(131, '2023-02-12 15:32:39', 2, NULL, NULL, NULL, NULL, 1, 1809.18),
(132, '2023-02-12 15:32:58', 2, NULL, NULL, NULL, NULL, 1, 17187.21),
(133, '2023-02-12 15:33:01', 2, NULL, NULL, NULL, NULL, 1, 8593.605),
(134, '2023-02-12 15:33:03', 2, NULL, NULL, NULL, NULL, 1, 4296.8025),
(135, '2023-02-12 15:33:05', 2, NULL, NULL, NULL, NULL, 1, 2148.40125),
(136, '2023-02-12 15:33:23', 2, NULL, NULL, NULL, NULL, 1, 107.4200625),
(137, '2023-02-12 15:33:48', 2, NULL, NULL, NULL, NULL, 1, -1701506816),
(138, '2023-02-12 15:34:48', 2, NULL, NULL, NULL, NULL, 1, 850754427.9999999),
(139, '2023-02-12 15:34:55', 2, NULL, NULL, NULL, NULL, 1, 425377214.00000006),
(140, '2023-02-12 15:37:59', 2, NULL, NULL, NULL, NULL, 1, 212688607.00000003),
(141, '2023-02-12 15:40:11', 2, NULL, NULL, NULL, NULL, 1, 212688606.99999997),
(142, '2023-02-12 15:40:24', 2, NULL, NULL, NULL, NULL, 1, -3403015679.9999995),
(143, '2023-02-12 15:40:27', 2, NULL, NULL, NULL, NULL, 1, 3403015679.9999995),
(144, '2023-02-12 15:46:20', 2, NULL, NULL, NULL, NULL, 1, -3403015679.9999995),
(145, '2023-02-12 15:46:23', 2, NULL, NULL, NULL, NULL, 1, 3403015679.9999995),
(146, '2023-02-12 15:46:27', 2, NULL, NULL, NULL, NULL, 1, -3403015679.9999995),
(147, '2023-02-12 15:46:29', 2, NULL, NULL, NULL, NULL, 1, 3403015679.9999995),
(148, '2023-02-12 15:46:34', 2, NULL, NULL, NULL, NULL, 1, -3403015679.9999995),
(149, '2023-02-12 15:46:37', 2, NULL, NULL, NULL, NULL, 1, 3403015679.9999995),
(150, '2023-02-12 15:47:19', 2, NULL, NULL, NULL, NULL, 1, -680603136),
(151, '2023-02-12 15:47:24', 2, NULL, NULL, NULL, NULL, 1, 680603136),
(152, '2023-02-12 15:47:38', 2, NULL, NULL, NULL, NULL, 1, -170150784),
(153, '2023-02-12 15:47:43', 2, NULL, NULL, NULL, NULL, 1, 170150784),
(154, '2023-02-12 15:47:56', 2, NULL, NULL, NULL, NULL, 1, -170150784),
(155, '2023-02-12 15:47:59', 2, NULL, NULL, NULL, NULL, 1, 34030156.800000004),
(156, '2023-02-12 15:48:02', 2, NULL, NULL, NULL, NULL, 1, 27224125.439999998),
(157, '2023-02-12 15:48:04', 2, NULL, NULL, NULL, NULL, 1, 21779300.351999998),
(158, '2023-02-12 15:48:06', 2, NULL, NULL, NULL, NULL, 1, 17423440.2816),
(159, '2023-02-12 15:48:21', 2, NULL, NULL, NULL, NULL, 1, 69693761.1264),
(160, '2023-02-12 15:48:23', 2, NULL, NULL, NULL, NULL, 1, -170150758.4),
(161, '2023-02-12 15:48:30', 2, NULL, NULL, NULL, NULL, 1, -3232864512);

-- --------------------------------------------------------

--
-- Table structure for table `block_history`
--

CREATE TABLE `block_history` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `location_fk` int NOT NULL,
  `material` varchar(255) COLLATE utf8mb3_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `crops`
--

CREATE TABLE `crops` (
  `id` int NOT NULL,
  `PLANT` varchar(94) COLLATE utf8mb3_bin NOT NULL,
  `SEED` varchar(94) COLLATE utf8mb3_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `crops`
--

INSERT INTO `crops` (`id`, `PLANT`, `SEED`) VALUES
(1, 'CARROTS', 'CARROT'),
(2, 'NETHER_WART', 'NETHER_WART'),
(3, 'POTATOES', 'POTATO'),
(4, 'WHEAT', 'WHEAT_SEEDS'),
(5, 'BEETROOTS', 'BEETROOT_SEEDS'),
(6, 'COCOA', 'COCOA_BEANS');

-- --------------------------------------------------------

--
-- Table structure for table `drops`
--

CREATE TABLE `drops` (
  `id` int NOT NULL,
  `MATERIAL` varchar(94) COLLATE utf8mb3_bin NOT NULL,
  `MIN_INT` int NOT NULL,
  `MAX_INT` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `drops`
--

INSERT INTO `drops` (`id`, `MATERIAL`, `MIN_INT`, `MAX_INT`) VALUES
(1, 'DIAMOND', 1, 2),
(2, 'EMERALD', 1, 2),
(3, 'RAW_IRON', 1, 3),
(4, 'RAW_COPPER', 2, 5),
(5, 'RAW_GOLD', 1, 2),
(6, 'LAPIS_LAZULI', 2, 5),
(7, 'COAL', 1, 3),
(8, 'QUARTZ', 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

CREATE TABLE `group` (
  `id` int NOT NULL,
  `name` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `prefix` varchar(2) COLLATE utf8mb3_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`id`, `name`, `prefix`) VALUES
(1, 'User', '§8'),
(32, 'VIP', '§a'),
(268435456, 'Mod', '§6'),
(1073741824, 'Admin', '§5');

-- --------------------------------------------------------

--
-- Table structure for table `location`
--

CREATE TABLE `location` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `yaw` float NOT NULL,
  `pitch` float NOT NULL,
  `world` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `location_name` varchar(45) COLLATE utf8mb3_bin DEFAULT NULL,
  `location_type_fk` int NOT NULL,
  `player_fk` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `location_type`
--

CREATE TABLE `location_type` (
  `id` int NOT NULL,
  `location_type` varchar(45) COLLATE utf8mb3_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `location_type`
--

INSERT INTO `location_type` (`id`, `location_type`) VALUES
(1, 'home'),
(2, 'death'),
(3, 'warp'),
(4, 'block_history'),
(5, 'protection');

-- --------------------------------------------------------

--
-- Table structure for table `npc`
--

CREATE TABLE `npc` (
  `id` int NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `name` varchar(94) COLLATE utf8mb3_bin DEFAULT NULL,
  `profession` varchar(94) COLLATE utf8mb3_bin DEFAULT NULL,
  `type` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_1_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_2_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_3_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_4_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_5_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_6_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_7_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_8_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_9_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_10_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_11_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_12_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_13_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_14_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_15_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_16_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_17_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_18_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_19_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_20_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_21_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_22_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_23_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_24_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_25_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_26_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_27_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR',
  `slot_28_name` varchar(94) COLLATE utf8mb3_bin NOT NULL DEFAULT 'AIR'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `npc`
--

INSERT INTO `npc` (`id`, `created`, `createdby`, `updated`, `updatedby`, `deleted`, `deletedby`, `name`, `profession`, `type`, `slot_1_name`, `slot_2_name`, `slot_3_name`, `slot_4_name`, `slot_5_name`, `slot_6_name`, `slot_7_name`, `slot_8_name`, `slot_9_name`, `slot_10_name`, `slot_11_name`, `slot_12_name`, `slot_13_name`, `slot_14_name`, `slot_15_name`, `slot_16_name`, `slot_17_name`, `slot_18_name`, `slot_19_name`, `slot_20_name`, `slot_21_name`, `slot_22_name`, `slot_23_name`, `slot_24_name`, `slot_25_name`, `slot_26_name`, `slot_27_name`, `slot_28_name`) VALUES
(1, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dAdventurer', 'NONE', 'TRADER', 'ARROW', 'BONE', 'ROTTEN_FLESH', 'SPIDER_EYE', 'STRING', 'GUNPOWDER', 'SLIME_BALL', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'GHAST_TEAR', 'BLAZE_ROD', 'AIR', 'ENDER_PEARL', 'SHULKER_SHELL', 'PHANTOM_MEMBRANE', 'EXPERIENCE_BOTTLE', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR'),
(2, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dBaker', 'NONE', 'TRADER', 'COOKIE', 'BREAD', 'PUMPKIN_PIE', 'CAKE', 'BAKED_POTATO', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'MUSHROOM_STEW', 'BEETROOT_SOUP', 'AIR', 'MILK_BUCKET', 'SUGAR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR'),
(3, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dButcher', 'BUTCHER', 'TRADER', 'RABBIT', 'PORKCHOP', 'BEEF', 'CHICKEN', 'MUTTON', 'AIR', 'AIR', 'COOKED_RABBIT', 'COOKED_PORKCHOP', 'COOKED_BEEF', 'COOKED_CHICKEN', 'COOKED_MUTTON', 'AIR', 'AIR', 'RABBIT_FOOT', 'AIR', 'LEATHER', 'FEATHER', 'AIR', 'AIR', 'AIR', 'RABBIT_HIDE', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR'),
(4, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dFarmer', 'FARMER', 'TRADER', 'SWEET_BERRIES', 'GLOW_BERRIES', 'APPLE', 'CARROT', 'POTATO', 'BEETROOT', 'SUGAR_CANE', 'PUMPKIN', 'MELON_SLICE', 'KELP', 'CHORUS_FRUIT', 'EGG', 'DRIED_KELP', 'WHEAT', 'WHEAT_SEEDS', 'BEETROOT_SEEDS', 'MELON_SEEDS', 'PUMPKIN_SEEDS', 'COCOA_BEANS', 'NETHER_WART', 'SUGAR', 'BROWN_MUSHROOM', 'RED_MUSHROOM', 'CRIMSON_FUNGUS', 'WARPED_FUNGUS', 'AIR', 'AIR', 'AIR'),
(5, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dFisher', 'FISHERMAN', 'TRADER', 'COD', 'SALMON', 'PUFFERFISH', 'TROPICAL_FISH', 'AIR', 'AIR', 'AIR', 'COD_BUCKET', 'SALMON_BUCKET', 'PUFFERFISH_BUCKET', 'TROPICAL_FISH_BUCKET', 'AXOLOTL_BUCKET', 'TADPOLE_BUCKET', 'WATER_BUCKET', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'OAK_BOAT', 'FISHING_ROD', 'CLAY_BALL', 'SEA_PICKLE', 'SEAGRASS', 'SCUTE', 'TURTLE_EGG'),
(6, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dFlorist', 'NONE', 'TRADER', 'DANDELION', 'POPPY', 'OXEYE_DAISY', 'BLUE_ORCHID', 'ALLIUM', 'LILY_OF_THE_VALLEY', 'AZURE_BLUET', 'ORANGE_TULIP', 'RED_TULIP', 'WHITE_TULIP', 'PINK_TULIP', 'CORNFLOWER', 'LILAC', 'SUNFLOWER', 'ROSE_BUSH', 'WITHER_ROSE', 'PEONY', 'DEAD_BUSH', 'SPORE_BLOSSOM', 'SMALL_DRIPLEAF', 'BIG_DRIPLEAF', 'GRASS', 'TALL_GRASS', 'FERN', 'LARGE_FERN', 'VINE', 'AIR', 'AIR'),
(7, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dLumberjack', 'NONE', 'TRADER', 'OAK_LOG', 'BIRCH_LOG', 'JUNGLE_LOG', 'ACACIA_LOG', 'DARK_OAK_LOG', 'SPRUCE_LOG', 'MANGROVE_LOG', 'OAK_SAPLING', 'BIRCH_SAPLING', 'JUNGLE_SAPLING', 'ACACIA_SAPLING', 'DARK_OAK_SAPLING', 'SPRUCE_SAPLING', 'MANGROVE_PROPAGULE', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'STICK', 'AIR', 'AIR', 'AIR', 'AIR', 'WARPED_STEM', 'CRIMSON_STEM'),
(8, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dMiner', 'MASON', 'TRADER', 'STONE', 'COBBLESTONE', 'GRANITE', 'DIORITE', 'ANDESITE', 'DEEPSLATE', 'BASALT', 'NETHERRACK', 'END_STONE', 'TUFF', 'CALCITE', 'BLACKSTONE', 'MOSSY_COBBLESTONE', 'QUARTZ_BLOCK', 'AIR', 'AIR', 'AIR', 'COAL', 'RAW_COPPER', 'RAW_IRON', 'RAW_GOLD', 'AMETHYST_SHARD', 'LAPIS_LAZULI', 'REDSTONE', 'QUARTZ', 'DIAMOND', 'EMERALD', 'ANCIENT_DEBRIS'),
(9, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dMusican', 'NONE', 'TRADER', 'MUSIC_DISC_13', 'MUSIC_DISC_11', 'MUSIC_DISC_5', 'MUSIC_DISC_BLOCKS', 'MUSIC_DISC_CAT', 'MUSIC_DISC_CHIRP', 'MUSIC_DISC_FAR', 'MUSIC_DISC_MALL', 'MUSIC_DISC_MELLOHI', 'MUSIC_DISC_OTHERSIDE', 'MUSIC_DISC_PIGSTEP', 'MUSIC_DISC_STAL', 'MUSIC_DISC_STRAD', 'MUSIC_DISC_WAIT', 'MUSIC_DISC_WARD', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'NOTE_BLOCK', 'JUKEBOX'),
(10, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dShepherd', 'SHEPHERD', 'TRADER', 'BLACK_WOOL', 'BROWN_WOOL', 'RED_WOOL', 'ORANGE_WOOL', 'YELLOW_WOOL', 'LIME_WOOL', 'GREEN_WOOL', 'CYAN_WOOL', 'LIGHT_BLUE_WOOL', 'BLUE_WOOL', 'PURPLE_WOOL', 'MAGENTA_WOOL', 'PINK_WOOL', 'WHITE_WOOL', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'SHEARS', 'LEAD', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR'),
(11, '2023-02-12 01:55:34', 1, NULL, NULL, NULL, NULL, '§dSmith', 'WEAPONSMITH', 'TRADER', 'IRON_SWORD', 'IRON_AXE', 'IRON_HORSE_ARMOR', 'IRON_HELMET', 'IRON_CHESTPLATE', 'IRON_LEGGINGS', 'IRON_BOOTS', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'GOLDEN_SWORD', 'GOLDEN_AXE', 'GOLDEN_HORSE_ARMOR', 'GOLDEN_HELMET', 'GOLDEN_CHESTPLATE', 'GOLDEN_LEGGINGS', 'GOLDEN_BOOTS', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR'),
(12, '2023-02-13 07:00:26', 1, NULL, NULL, NULL, NULL, '§dBuilder', 'NONE', 'TRADER', 'GRASS_BLOCK', 'DIRT', 'SAND', 'COARSE_DIRT', 'GRAVEL', 'TERRACOTTA', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'AIR', 'NETHERRACK', 'PRISMARINE', 'END_STONE', 'MUD_BRICKS', 'DEEPSLATE_BRICKS', 'STONE_BRICKS', 'AIR', 'NETHER_BRICKS', 'PRISMARINE_BRICKS', 'END_STONE_BRICKS', 'BRICKS', 'POLISHED_BLACKSTONE_BRICKS', 'AIR', 'AIR');

-- --------------------------------------------------------

--
-- Table structure for table `permission`
--

CREATE TABLE `permission` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `name` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `path` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `permission_group`
--

CREATE TABLE `permission_group` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `group_fk` int NOT NULL,
  `permission_fk` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `permission_player`
--

CREATE TABLE `permission_player` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `player_fk` int NOT NULL,
  `permission_fk` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `uuid` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `group_fk` int NOT NULL,
  `afk` tinyint DEFAULT '0',
  `fly` tinyint DEFAULT '0',
  `name` varchar(100) COLLATE utf8mb3_bin DEFAULT NULL,
  `customname` varchar(100) COLLATE utf8mb3_bin DEFAULT NULL,
  `purse` double NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `player_partner`
--

CREATE TABLE `player_partner` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `first_partner_fk` int NOT NULL,
  `second_partner_fk` int NOT NULL,
  `shareProtections` tinyint NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `plugin_informations`
--

CREATE TABLE `plugin_informations` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `UPDATED` datetime DEFAULT NULL,
  `UPDATEDBY` int DEFAULT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `tab_header` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `tab_footer` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `motd_message` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `motd_players` int NOT NULL,
  `db_version` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `plugin_informations`
--

INSERT INTO `plugin_informations` (`ID`, `CREATED`, `CREATEDBY`, `UPDATED`, `UPDATEDBY`, `DELETED`, `DELETEDBY`, `tab_header`, `tab_footer`, `motd_message`, `motd_players`, `db_version`) VALUES
(1, '2021-01-05 20:57:05', 1, NULL, NULL, '2023-01-07 18:40:37', 1, '§7<<------------------>>          §6Relluem94.de           §7<<------------------>>', '§7<<------------------>> §6powered by §8Rellu§cEssentials §7<<------------------>>', '§6powered by §8Rellu§cEssentials', 94, 1),
(2, '2023-01-07 18:40:37', 1, NULL, NULL, '2023-01-07 18:41:00', 1, '§7<<------------------>>          §6Relluem94.de           §7<<------------------>>', '§7<<------------------>> §6powered by §8Rellu§cEssentials §7<<------------------>>', '§6powered by §8Rellu§cEssentials', 94, 2),
(3, '2023-01-07 18:41:00', 1, NULL, NULL, '2023-01-15 21:21:52', 1, '§7<<------------------>>          §6Relluem94.de           §7<<------------------>>', '§7<<------------------>> §6powered by §8Rellu§cEssentials §7<<------------------>>', '§6powered by §8Rellu§cEssentials', 94, 3),
(4, '2023-01-15 21:21:52', 1, NULL, NULL, '2023-02-19 00:06:32', 1, '§7<<------------------>>          §6Relluem94.de           §7<<------------------>>', '§7<<------------------>> §6powered by §8Rellu§cEssentials §7<<------------------>>', '§6powered by §8Rellu§cEssentials', 94, 4),
(5, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, '§7<<------------------>>          §6Relluem94.de           §7<<------------------>>', '§7<<------------------>> §6powered by §8Rellu§cEssentials §7<<------------------>>', '§6powered by §8Rellu§cEssentials', 94, 5);

-- --------------------------------------------------------

--
-- Table structure for table `plugin_setting`
--

CREATE TABLE `plugin_setting` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `UPDATED` datetime DEFAULT NULL,
  `UPDATEDBY` int DEFAULT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `setting_fk` int NOT NULL,
  `value` json NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `protections`
--

CREATE TABLE `protections` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `location_fk` int NOT NULL,
  `material_name` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `flags` json DEFAULT NULL,
  `rights` json DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `protection_locks`
--

CREATE TABLE `protection_locks` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `value` varchar(255) COLLATE utf8mb3_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `protection_locks`
--

INSERT INTO `protection_locks` (`id`, `created`, `createdby`, `updated`, `updatedby`, `deleted`, `deletedby`, `value`) VALUES
(1, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'OAK_SIGN'),
(2, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SPRUCE_SIGN'),
(3, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BIRCH_SIGN'),
(4, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'JUNGLE_SIGN'),
(5, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ACACIA_SIGN'),
(6, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'DARK_OAK_SIGN'),
(7, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'MANGROVE_SIGN'),
(8, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CRIMSON_SIGN'),
(9, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'WARPED_SIGN'),
(10, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'OAK_WALL_SIGN'),
(11, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SPRUCE_WALL_SIGN'),
(12, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BIRCH_WALL_SIGN'),
(13, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'JUNGLE_WALL_SIGN'),
(14, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ACACIA_WALL_SIGN'),
(15, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'DARK_OAK_WALL_SIGN'),
(16, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'MANGROVE_WALL_SIGN'),
(17, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CRIMSON_WALL_SIGN'),
(18, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'WARPED_WALL_SIGN'),
(19, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'OAK_WALL_HANGING_SIGN'),
(20, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SPRUCE_WALL_HANGING_SIGN'),
(21, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BIRCH_WALL_HANGING_SIGN'),
(22, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'JUNGLE_WALL_HANGING_SIGN'),
(23, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ACACIA_WALL_HANGING_SIGN'),
(24, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'DARK_OAK_WALL_HANGING_SIGN'),
(25, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'MANGROVE_WALL_HANGING_SIGN'),
(26, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CRIMSON_WALL_HANGING_SIGN'),
(27, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'WARPED_WALL_HANGING_SIGN'),
(28, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'OAK_HANGING_SIGN'),
(29, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SPRUCE_HANGING_SIGN'),
(30, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BIRCH_HANGING_SIGN'),
(31, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'JUNGLE_HANGING_SIGN'),
(32, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ACACIA_HANGING_SIGN'),
(33, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'DARK_OAK_HANGING_SIGN'),
(34, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'MANGROVE_HANGING_SIGN'),
(35, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CRIMSON_HANGING_SIGN'),
(36, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'WARPED_HANGING_SIGN'),
(37, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'OAK_TRAPDOOR'),
(38, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SPRUCE_TRAPDOOR'),
(39, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BIRCH_TRAPDOOR'),
(40, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'JUNGLE_TRAPDOOR'),
(41, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ACACIA_TRAPDOOR'),
(42, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'DARK_OAK_TRAPDOOR'),
(43, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'MANGROVE_TRAPDOOR'),
(44, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CRIMSON_TRAPDOOR'),
(45, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'WARPED_TRAPDOOR'),
(46, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'IRON_TRAPDOOR'),
(47, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'OAK_DOOR'),
(48, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SPRUCE_DOOR'),
(49, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BIRCH_DOOR'),
(50, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'JUNGLE_DOOR'),
(51, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ACACIA_DOOR'),
(52, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'DARK_OAK_DOOR'),
(53, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'MANGROVE_DOOR'),
(54, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CRIMSON_DOOR'),
(55, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'WARPED_DOOR'),
(56, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'IRON_DOOR'),
(57, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'OAK_FENCE_GATE'),
(58, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SPRUCE_FENCE_GATE'),
(59, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BIRCH_FENCE_GATE'),
(60, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'JUNGLE_FENCE_GATE'),
(61, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ACACIA_FENCE_GATE'),
(62, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'DARK_OAK_FENCE_GATE'),
(63, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'MANGROVE_FENCE_GATE'),
(64, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CRIMSON_FENCE_GATE'),
(65, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'WARPED_FENCE_GATE'),
(66, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'FURNACE'),
(67, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BLAST_FURNACE'),
(68, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SMOKER'),
(69, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'LECTERN'),
(70, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CHEST'),
(71, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'TRAPPED_CHEST'),
(72, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'SHULKER_BOX'),
(73, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'WHITE_SHULKER_BOX'),
(74, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'LIGHT_GRAY_SHULKER_BOX'),
(75, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'GRAY_SHULKER_BOX'),
(76, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BLACK_SHULKER_BOX'),
(77, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BROWN_SHULKER_BOX'),
(78, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'RED_SHULKER_BOX'),
(79, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ORANGE_SHULKER_BOX'),
(80, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'YELLOW_SHULKER_BOX'),
(81, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'LIME_SHULKER_BOX'),
(82, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'GREEN_SHULKER_BOX'),
(83, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CYAN_SHULKER_BOX'),
(84, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'LIGHT_BLUE_SHULKER_BOX'),
(85, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BLUE_SHULKER_BOX'),
(86, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'PURPLE_SHULKER_BOX'),
(87, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'MAGENTA_SHULKER_BOX'),
(88, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'PINK_SHULKER_BOX'),
(89, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ITEM_FRAME'),
(90, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'JUKEBOX'),
(91, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'CAULDRON'),
(92, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'ENCHANTING_TABLE'),
(93, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BEACON'),
(94, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'LODESTONE'),
(95, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'COMPOSTER'),
(96, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'BARREL'),
(97, '2023-02-12 03:44:59', 1, NULL, NULL, NULL, NULL, 'LEVER'),
(98, '2023-02-12 03:48:21', 1, NULL, NULL, NULL, NULL, 'ANVIL');

-- --------------------------------------------------------

--
-- Table structure for table `setting`
--

CREATE TABLE `setting` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `UPDATED` datetime DEFAULT NULL,
  `UPDATEDBY` int DEFAULT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb3_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `setting`
--

INSERT INTO `setting` (`ID`, `CREATED`, `CREATEDBY`, `UPDATED`, `UPDATEDBY`, `DELETED`, `DELETEDBY`, `name`) VALUES
(1, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'ORE_RESPAWN'),
(2, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'NPC_BANKER'),
(3, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'NPC_BAGSALESMAN'),
(4, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'NPC_BEEKEEPER'),
(5, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'NPC_ENCHANTER');

-- --------------------------------------------------------

--
-- Table structure for table `setting_player`
--

CREATE TABLE `setting_player` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `UPDATED` datetime DEFAULT NULL,
  `UPDATEDBY` int DEFAULT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `player_fk` int NOT NULL,
  `setting_fk` int NOT NULL,
  `value` json NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `skills`
--

CREATE TABLE `skills` (
  `id` int NOT NULL,
  `name` varchar(94) COLLATE utf8mb3_bin NOT NULL,
  `displayname` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `max_level` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `skills`
--

INSERT INTO `skills` (`id`, `name`, `displayname`, `max_level`) VALUES
(1, 'TREE_FELLER', 'Tree Feller', 1000),
(2, 'SALVAGE', 'Salvage', 1000),
(3, 'REPAIR', 'Repair', 1000);

-- --------------------------------------------------------

--
-- Table structure for table `skills_player`
--

CREATE TABLE `skills_player` (
  `id` int NOT NULL,
  `created` datetime NOT NULL,
  `createdby` int NOT NULL,
  `updated` datetime DEFAULT NULL,
  `updatedby` int DEFAULT NULL,
  `deleted` datetime DEFAULT NULL,
  `deletedby` int DEFAULT NULL,
  `player_fk` int NOT NULL,
  `skill_fk` int NOT NULL,
  `skill_level` int NOT NULL,
  `skill_xp` float NOT NULL,
  `block_counter_level` int DEFAULT NULL,
  `block_counter_total` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `world`
--

CREATE TABLE `world` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `UPDATED` datetime DEFAULT NULL,
  `UPDATEDBY` int DEFAULT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `NAME` varchar(45) COLLATE utf8mb3_bin NOT NULL,
  `world_group_fk` int NOT NULL,
  `build_group_fk` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `world`
--

INSERT INTO `world` (`ID`, `CREATED`, `CREATEDBY`, `UPDATED`, `UPDATEDBY`, `DELETED`, `DELETEDBY`, `NAME`, `world_group_fk`, `build_group_fk`) VALUES
(1, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'lobby', 1, 268435456),
(2, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'world', 2, 1),
(3, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'world_nether', 2, 1),
(4, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'world_the_end', 2, 1),
(5, '2023-02-19 20:19:55', 1, NULL, NULL, NULL, NULL, 'stargate_rellu', 3, 16);

-- --------------------------------------------------------

--
-- Table structure for table `world_group`
--

CREATE TABLE `world_group` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `UPDATED` datetime DEFAULT NULL,
  `UPDATEDBY` int DEFAULT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `NAME` varchar(45) COLLATE utf8mb3_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `world_group`
--

INSERT INTO `world_group` (`ID`, `CREATED`, `CREATEDBY`, `UPDATED`, `UPDATEDBY`, `DELETED`, `DELETEDBY`, `NAME`) VALUES
(1, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'lobby'),
(2, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 'freebuild'),
(3, '2023-02-19 20:19:55', 1, NULL, NULL, NULL, NULL, 'stargate_rellu');

-- --------------------------------------------------------

--
-- Table structure for table `world_group_inventory`
--

CREATE TABLE `world_group_inventory` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `UPDATED` datetime DEFAULT NULL,
  `UPDATEDBY` int DEFAULT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `player_fk` int NOT NULL,
  `world_group_fk` int NOT NULL,
  `inventory` json NOT NULL,
  `health` double NOT NULL,
  `food` int NOT NULL,
  `totalExperience` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- --------------------------------------------------------

--
-- Table structure for table `world_group_setting`
--

CREATE TABLE `world_group_setting` (
  `ID` int NOT NULL,
  `CREATED` datetime NOT NULL,
  `CREATEDBY` int NOT NULL,
  `UPDATED` datetime DEFAULT NULL,
  `UPDATEDBY` int DEFAULT NULL,
  `DELETED` datetime DEFAULT NULL,
  `DELETEDBY` int DEFAULT NULL,
  `setting_fk` int NOT NULL,
  `world_group_fk` int NOT NULL,
  `value` json NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

--
-- Dumping data for table `world_group_setting`
--

INSERT INTO `world_group_setting` (`ID`, `CREATED`, `CREATEDBY`, `UPDATED`, `UPDATEDBY`, `DELETED`, `DELETEDBY`, `setting_fk`, `world_group_fk`, `value`) VALUES
(1, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 1, 2, '[false]'),
(2, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 2, 2, '[true]'),
(3, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 3, 2, '[true]'),
(4, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 4, 2, '[true]'),
(5, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 5, 2, '[true]'),
(6, '2023-02-19 00:06:32', 1, NULL, NULL, NULL, NULL, 6, 2, '[true]');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bag`
--
ALTER TABLE `bag`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bag_type`
--
ALTER TABLE `bag_type`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bank_account`
--
ALTER TABLE `bank_account`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_bank_account_tier_1_idx` (`bank_tier_fk`),
  ADD KEY `fk_bank_account_player_1_idx` (`player_fk`);

--
-- Indexes for table `bank_tier`
--
ALTER TABLE `bank_tier`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bank_transaction`
--
ALTER TABLE `bank_transaction`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_bank_account_transaction_1_idx` (`bank_account_fk`);

--
-- Indexes for table `block_history`
--
ALTER TABLE `block_history`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `fk_block_history_1_idx` (`location_fk`);

--
-- Indexes for table `crops`
--
ALTER TABLE `crops`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `drops`
--
ALTER TABLE `drops`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `location`
--
ALTER TABLE `location`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_location_type_1_idx` (`location_type_fk`),
  ADD KEY `fk_location_player_1_idx` (`player_fk`);

--
-- Indexes for table `location_type`
--
ALTER TABLE `location_type`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `npc`
--
ALTER TABLE `npc`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `permission`
--
ALTER TABLE `permission`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `permission_group`
--
ALTER TABLE `permission_group`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_permission_group_1_idx` (`group_fk`),
  ADD KEY `fk_permission_group_2_idx` (`permission_fk`);

--
-- Indexes for table `permission_player`
--
ALTER TABLE `permission_player`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_permission_group_2_idx` (`permission_fk`),
  ADD KEY `fk_permission_group_1_idx` (`player_fk`);

--
-- Indexes for table `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uuid_UNIQUE` (`uuid`),
  ADD KEY `fk_player_group_1_idx` (`group_fk`);

--
-- Indexes for table `player_partner`
--
ALTER TABLE `player_partner`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `plugin_informations`
--
ALTER TABLE `plugin_informations`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `plugin_setting`
--
ALTER TABLE `plugin_setting`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `protections`
--
ALTER TABLE `protections`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_protections_Location_1_idx` (`location_fk`);

--
-- Indexes for table `protection_locks`
--
ALTER TABLE `protection_locks`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `setting`
--
ALTER TABLE `setting`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `setting_player`
--
ALTER TABLE `setting_player`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `skills`
--
ALTER TABLE `skills`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `skills_player`
--
ALTER TABLE `skills_player`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `world`
--
ALTER TABLE `world`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `world_group`
--
ALTER TABLE `world_group`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `world_group_inventory`
--
ALTER TABLE `world_group_inventory`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `world_group_setting`
--
ALTER TABLE `world_group_setting`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bag`
--
ALTER TABLE `bag`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `bag_type`
--
ALTER TABLE `bag_type`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `bank_account`
--
ALTER TABLE `bank_account`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `bank_tier`
--
ALTER TABLE `bank_tier`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `bank_transaction`
--
ALTER TABLE `bank_transaction`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=299;

--
-- AUTO_INCREMENT for table `block_history`
--
ALTER TABLE `block_history`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `crops`
--
ALTER TABLE `crops`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `drops`
--
ALTER TABLE `drops`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `location`
--
ALTER TABLE `location`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1811;

--
-- AUTO_INCREMENT for table `location_type`
--
ALTER TABLE `location_type`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `npc`
--
ALTER TABLE `npc`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `permission`
--
ALTER TABLE `permission`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `permission_group`
--
ALTER TABLE `permission_group`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `permission_player`
--
ALTER TABLE `permission_player`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `player`
--
ALTER TABLE `player`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `player_partner`
--
ALTER TABLE `player_partner`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `plugin_informations`
--
ALTER TABLE `plugin_informations`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `plugin_setting`
--
ALTER TABLE `plugin_setting`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `protections`
--
ALTER TABLE `protections`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1749;

--
-- AUTO_INCREMENT for table `protection_locks`
--
ALTER TABLE `protection_locks`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=99;

--
-- AUTO_INCREMENT for table `setting`
--
ALTER TABLE `setting`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `setting_player`
--
ALTER TABLE `setting_player`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `skills`
--
ALTER TABLE `skills`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `skills_player`
--
ALTER TABLE `skills_player`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `world`
--
ALTER TABLE `world`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `world_group`
--
ALTER TABLE `world_group`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `world_group_inventory`
--
ALTER TABLE `world_group_inventory`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `world_group_setting`
--
ALTER TABLE `world_group_setting`
  MODIFY `ID` int NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `block_history`
--
ALTER TABLE `block_history`
  ADD CONSTRAINT `fk_block_history_1` FOREIGN KEY (`location_fk`) REFERENCES `location` (`id`);

--
-- Constraints for table `location`
--
ALTER TABLE `location`
  ADD CONSTRAINT `fk_location_player_1` FOREIGN KEY (`player_fk`) REFERENCES `player` (`id`),
  ADD CONSTRAINT `fk_location_type_1` FOREIGN KEY (`location_type_fk`) REFERENCES `location_type` (`id`);

--
-- Constraints for table `player`
--
ALTER TABLE `player`
  ADD CONSTRAINT `fk_player_group_1` FOREIGN KEY (`group_fk`) REFERENCES `group` (`id`);
COMMIT;