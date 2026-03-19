SET FOREIGN_KEY_CHECKS=0;

-- Use the current database/schema
SET @schema := DATABASE();

-- Find and drop the FK from employee.address_id -> address.id if it exists
-- Use a safe subquery assignment so it becomes NULL when nothing is found (no warning 1329)
SET @fkName = (
  SELECT CONSTRAINT_NAME
  FROM information_schema.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = @schema
    AND TABLE_NAME = 'employee'
    AND COLUMN_NAME = 'address_id'
    AND REFERENCED_TABLE_NAME = 'address'
  LIMIT 1
);

SET @drop_sql = IF(@fkName IS NOT NULL,
    CONCAT('ALTER TABLE `', @schema, '`.`employee` DROP FOREIGN KEY `', @fkName, '`;'),
    'SELECT 1;');
PREPARE stmt FROM @drop_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Inspect current column types (use safe subquery assignments)
SET @address_id_type = (
  SELECT COLUMN_TYPE
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @schema
    AND TABLE_NAME = 'employee'
    AND COLUMN_NAME = 'address_id'
  LIMIT 1
);

SET @address_type = (
  SELECT COLUMN_TYPE
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @schema
    AND TABLE_NAME = 'address'
    AND COLUMN_NAME = 'id'
  LIMIT 1
);

-- If address.id is not CHAR(36), alter it
SET @alter_address_sql = IF(@address_type IS NULL OR LOWER(@address_type) LIKE '%char(36)%',
    'SELECT 1;',
    CONCAT('ALTER TABLE `', @schema, '`.`address` MODIFY COLUMN `id` CHAR(36) NOT NULL;'));
PREPARE stmt FROM @alter_address_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- If employee.address_id is not CHAR(36), alter it (allow NULLs)
SET @alter_employee_sql = IF(@address_id_type IS NULL OR LOWER(@address_id_type) LIKE '%char(36)%',
    'SELECT 1;',
    CONCAT('ALTER TABLE `', @schema, '`.`employee` MODIFY COLUMN `address_id` CHAR(36);'));
PREPARE stmt FROM @alter_employee_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Recreate the foreign key if both columns exist and types are compatible
SELECT COUNT(*) INTO @both_exist
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = @schema
  AND ((TABLE_NAME = 'employee' AND COLUMN_NAME = 'address_id') OR (TABLE_NAME = 'address' AND COLUMN_NAME = 'id'));

SET @create_fk_sql = 'SELECT 1;';
IF @both_exist = 2 THEN
  SET @create_fk_sql = CONCAT('ALTER TABLE `', @schema, '`.`employee` ADD CONSTRAINT `FK_EMPLOYEE_ON_ADDRESS` FOREIGN KEY (`address_id`) REFERENCES `', @schema, '`.`address` (`id`);');
END IF;

PREPARE stmt FROM @create_fk_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET FOREIGN_KEY_CHECKS=1;
