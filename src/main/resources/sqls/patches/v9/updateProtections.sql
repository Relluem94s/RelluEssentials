UPDATE protections
SET flags = JSON_SET(
    flags,
    '$.flags',
    JSON_ARRAY(
        CASE
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]')) = 'allowHopper' THEN 'ALLOW_HOPPER'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]')) = 'allowPublic' THEN 'ALLOW_PUBLIC'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]')) = 'allowRedstone' THEN 'ALLOW_REDSTONE'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]')) = 'autoClose' THEN 'AUTO_CLOSE'
            ELSE JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]'))
        END,
        CASE
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[1]')) = 'allowHopper' THEN 'ALLOW_HOPPER'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[1]')) = 'allowPublic' THEN 'ALLOW_PUBLIC'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[1]')) = 'allowRedstone' THEN 'ALLOW_REDSTONE'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[1]')) = 'autoClose' THEN 'AUTO_CLOSE'
            ELSE JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[1]'))
        END,
        CASE
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[2]')) = 'allowHopper' THEN 'ALLOW_HOPPER'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[2]')) = 'allowPublic' THEN 'ALLOW_PUBLIC'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[2]')) = 'allowRedstone' THEN 'ALLOW_REDSTONE'
            WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[2]')) = 'autoClose' THEN 'AUTO_CLOSE'
            ELSE JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[2]'))
        END
    )
);