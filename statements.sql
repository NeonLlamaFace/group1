SELECT Code, country.Name, Continent, Region, country.Population, city.Name
FROM country
INNER JOIN city ON country.Capital = city.ID
ORDER BY Population desc;

SELECT Code, country.Name, Continent, Region, country.Population, city.Name
FROM country
INNER JOIN city ON country.Capital = city.ID
WHERE Continent = 'Europe'
ORDER BY Population desc;

SELECT Code, country.Name, Continent, Region, country.Population, city.Name
FROM country
INNER JOIN city ON country.Capital = city.ID
WHERE Region = 'British Islands'
ORDER BY Population desc;


SELECT Code, country.Name, Continent, Region, country.Population, city.Name
FROM country
INNER JOIN city ON country.Capital = city.ID
ORDER BY Population desc
LIMIT 5;

SELECT Code, country.Name, Continent, Region, country.Population, city.Name
FROM country
INNER JOIN city ON country.Capital = city.ID
WHERE Continent = 'Europe'
ORDER BY country.Population desc
LIMIT 5;

SELECT Code, country.Name, Continent, Region, country.Population, city.Name
FROM country
INNER JOIN city ON country.Capital = city.ID
WHERE Region = 'British Islands'
ORDER BY country.Population desc
LIMIT 5;



SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
ORDER BY city.Population desc;

SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE Continent = 'Europe'
ORDER BY city.Population desc;

SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE Region = 'British Islands'
ORDER BY city.Population desc;

SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE country.Name = 'United Kingdom'
ORDER BY city.Population desc;

SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE District = 'Scotland'
ORDER BY city.Population desc;


SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
ORDER BY city.Population desc
LIMIT 5;

SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE Continent = 'Europe'
ORDER BY city.Population desc
LIMIT 5;

SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE Region = 'British Islands'
ORDER BY city.Population desc
LIMIT 5;

SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE country.Name = 'United Kingdom'
ORDER BY city.Population desc
LIMIT 5;

SELECT city.Name, country.Name, District, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE District = 'Scotland'
ORDER BY city.Population desc
LIMIT 5;



SELECT city.Name, country.Name, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE ID = Capital
ORDER BY city.Population desc;

SELECT city.Name, country.Name, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE ID = Capital AND Continent = "Europe"
ORDER BY city.Population desc;

SELECT city.Name, country.Name, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE ID = Capital AND Region = "British Islands"
ORDER BY city.Population desc;


SELECT city.Name, country.Name, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE ID = Capital
ORDER BY city.Population desc
LIMIT 5;

SELECT city.Name, country.Name, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE ID = Capital AND Continent = "Europe"
ORDER BY city.Population desc
LIMIT 5;

SELECT city.Name, country.Name, city.Population
FROM city
INNER JOIN country ON city.CountryCode = country.Code
WHERE ID = Capital AND Region = "British Islands"
ORDER BY city.Population desc
LIMIT 5;



SELECT Continent, SUM(DISTINCT country.Population) AS "Total population", SUM(city.Population) AS "Population in cities", SUM(DISTINCT country.Population) - SUM(city.Population) AS "Population outwith cities", CONCAT(SUM(city.Population) / SUM(DISTINCT country.Population) * 100, "%") AS "Percentage"
FROM country
LEFT JOIN city ON country.Code = city.CountryCode
GROUP BY Continent;

SELECT Region, SUM(DISTINCT country.Population) AS "Total population", SUM(city.Population) AS "Population in cities", SUM(DISTINCT country.Population) - SUM(city.Population) AS "Population outwith cities", CONCAT(SUM(city.Population) / SUM(DISTINCT country.Population) * 100, "%") AS "Percentage"
FROM country
LEFT JOIN city ON country.Code = city.CountryCode
GROUP BY Region;

SELECT country.Name AS "Country", SUM(DISTINCT country.Population) AS "Total population", SUM(city.Population) AS "Population in cities", SUM(DISTINCT country.Population) - SUM(city.Population) AS "Population outwith cities", CONCAT(SUM(city.Population) / SUM(DISTINCT country.Population) * 100, "%") AS "Percentage"
FROM country
LEFT JOIN city ON country.Code = city.CountryCode
GROUP BY country.Name;



SELECT SUM(Population) AS "World population"
FROM country;

SELECT SUM(Population) AS "Continent population"
FROM country
WHERE Continent = "Europe";

SELECT SUM(Population) AS "Region population"
FROM country
WHERE Region = "British Islands";

SELECT SUM(Population) AS "Country population"
FROM country
WHERE Name = "United Kingdom";

SELECT SUM(Population) AS "District population"
FROM city
WHERE District = "Scotland";

SELECT SUM(Population) AS "City population"
FROM city
WHERE Name = "Edinburgh";



SELECT Language, ROUND(SUM(Speakers), 0) AS Speakers, SUM(Speakers) / (SELECT SUM(Population) FROM country) * 100 AS "Speakers as a percentage of world population"
FROM (
        SELECT Language, SUM(Percentage) * Population / 100 AS Speakers
        FROM countrylanguage
        INNER JOIN country ON countrylanguage.CountryCode = country.Code
        WHERE Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic')
        GROUP BY Language, Population
    ) AS innerTable
GROUP BY Language
ORDER BY Speakers DESC;