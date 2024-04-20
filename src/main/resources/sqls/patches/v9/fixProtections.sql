UPDATE protections
SET flags = CASE
    WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]')) IS NOT NULL THEN
        JSON_SET(flags, '$.flags', JSON_ARRAY(JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]'))))
    WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[1]')) IS NOT NULL THEN
        JSON_SET(flags, '$.flags', JSON_ARRAY(JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]')), JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[1]'))))
    WHEN JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]')) IS NOT NULL THEN
        JSON_SET(flags, '$.flags', JSON_ARRAY(JSON_UNQUOTE(JSON_EXTRACT(flags, '$.flags[0]'))))
    ELSE
        JSON_SET(flags, '$.flags', JSON_ARRAY())
    END;