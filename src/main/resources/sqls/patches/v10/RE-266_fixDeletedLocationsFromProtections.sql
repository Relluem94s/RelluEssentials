UPDATE location l
INNER JOIN protections p ON p.location_fk = l.id
SET l.deleted = NULL,
    l.deletedby = NULL
WHERE p.deleted IS NULL
  AND l.deleted IS NOT NULL;