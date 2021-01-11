userRight.findAllMenu:
SELECT
  a.menu_id AS id,
  a.lvl_code AS code,
  a.menu_name AS name,
  1 AS type
FROM
  aolie_s_menu a
WHERE version_code =  #{versionCode}
UNION
ALL
SELECT
  b.btn_id AS id,
  CONCAT(c.lvl_code, b.lvl_code) AS code,
  b.title AS name,
  2 AS type
FROM
  aolie_s_menu_button b,
  aolie_s_menu c
WHERE b.version_code = c.version_code
  AND b.menu_id = c.menu_id
  AND b.version_code = #{versionCode}

name2:
select * from aolie_s_user;