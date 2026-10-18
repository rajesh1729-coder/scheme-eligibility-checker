-- Seed 15 real Indian Government Schemes (with TODO: verify where exact caps/links vary by state/notification)
INSERT INTO scheme (id, name, description, benefit, apply_link, source_url) VALUES
(1, 'Atal Pension Yojana (APY)', 'Guaranteed minimum pension scheme for unorganized sector workers.', 'Monthly pension of Rs 1,000 to Rs 5,000 after age 60', 'https://enps.nsdl.com', 'https://www.npscra.nsdl.co.in/scheme-details.php'),
(2, 'Pradhan Mantri Jeevan Jyoti Bima Yojana (PMJJBY)', 'One-year renewable term life insurance cover for savings bank account holders.', 'Rs 2 Lakh life cover on death due to any reason', 'https://jansuraksha.gov.in', 'https://jansuraksha.gov.in/pmjjby.aspx'),
(3, 'Pradhan Mantri Suraksha Bima Yojana (PMSBY)', 'Accidental death and disability insurance scheme.', 'Rs 2 Lakh for accidental death/full disability', 'https://jansuraksha.gov.in', 'https://jansuraksha.gov.in/pmsby.aspx'),
(4, 'PM-SYM (Shram Yogi Maan-dhan)', 'Voluntary and contributory pension scheme for unorganized workers.', 'Assured monthly pension of Rs 3,000 after age 60', 'https://maandhan.in', 'https://labour.gov.in/pm-sym'),
(5, 'Post Matric Scholarship for SC Students', 'Financial assistance to Scheduled Caste students studying at post-matriculation stage.', 'Tuition fee reimbursement and academic allowance', 'https://scholarships.gov.in', 'https://socialjustice.gov.in/schemes/25'),
(6, 'PM YASASVI Post-Matric Scholarship for OBC/EBC/DNT', 'Scholarship for meritorious OBC, EBC, and DNT students for higher education.', 'Academic allowance up to Rs 20,000 per annum', 'https://scholarships.gov.in', 'https://socialjustice.gov.in/schemes/102'),
(7, 'National Fellowship and Scholarship for ST Students', 'Higher education support for Scheduled Tribe students.', 'Fellowship and tuition support (TODO: verify exact annual cap)', 'https://scholarships.gov.in', 'https://tribal.nic.in/Scholarships.aspx'),
(8, 'Indira Gandhi National Old Age Pension Scheme (IGNOAPS)', 'Non-contributory old age pension under NSAP for low-income senior citizens.', 'Monthly pension support (TODO: verify state top-up amounts)', 'https://nsap.nic.in', 'https://nsap.nic.in/guidelines.do'),
(9, 'Pradhan Mantri Awas Yojana - Urban (PMAY-U EWS)', 'Affordable housing credit linked subsidy and assistance for Economically Weaker Sections.', 'Financial assistance for house construction/purchase', 'https://pmaymis.gov.in', 'https://pmay-urban.gov.in'),
(10, 'Ayushman Bharat PM-JAY (Senior Citizen 70+ Expansion)', 'Universal health cover for all senior citizens aged 70 and above regardless of income.', 'Free health insurance cover up to Rs 5 Lakh per family per year', 'https://beneficiary.nha.gov.in', 'https://nha.gov.in/PM-JAY'),
(11, 'Mukhyamantri Majhi Ladki Bahin Yojana (Maharashtra)', 'Financial assistance scheme for eligible resident women/citizens in Maharashtra (TODO: verify gender rule extension).', 'Rs 1,500 per month direct benefit transfer', 'https://ladkibahin.maharashtra.gov.in', 'https://ladkibahin.maharashtra.gov.in'),
(12, 'Kalaignar Magalir Urimai Thittam (Tamil Nadu)', 'Basic income support scheme for eligible households in Tamil Nadu.', 'Rs 1,000 per month financial assistance', 'https://kmuy.tn.gov.in', 'https://www.tn.gov.in/scheme'),
(13, 'Yuva Nidhi Scheme (Karnataka)', 'Unemployment assistance for recent graduates and diploma holders in Karnataka.', 'Up to Rs 3,000 per month allowance (TODO: verify education rule)', 'https://sevasindhugs.karnataka.gov.in', 'https://kaushalkar.com'),
(14, 'Pradhan Mantri Mudra Yojana (PMMY)', 'Collateral-free micro-enterprise loans for non-corporate small businesses.', 'Loans up to Rs 10 Lakh (Shishu, Kishore, Tarun)', 'https://udyamimitra.in', 'https://www.mudra.org.in'),
(15, 'Babu Jagjivan Ram Chhatrawas Yojana', 'Hostel construction and support assistance for SC students (TODO: verify direct individual income threshold).', 'Subsidized hostel accommodation for SC students', 'https://socialjustice.gov.in', 'https://socialjustice.gov.in/schemes/27');

-- Rules for Scheme 1: APY (Age 18 to 40)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(1, 'AGE', 'GTE', '18'),
(1, 'AGE', 'LTE', '40'),
(1, 'STATE', 'EQ', 'ALL');

-- Rules for Scheme 2: PMJJBY (Age 18 to 50)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(2, 'AGE', 'GTE', '18'),
(2, 'AGE', 'LTE', '50');

-- Rules for Scheme 3: PMSBY (Age 18 to 70)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(3, 'AGE', 'GTE', '18'),
(3, 'AGE', 'LTE', '70');

-- Rules for Scheme 4: PM-SYM (Age 18 to 40, Annual Income <= 180000 i.e. Rs 15,000/month)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(4, 'AGE', 'GTE', '18'),
(4, 'AGE', 'LTE', '40'),
(4, 'INCOME', 'LTE', '180000');

-- Rules for Scheme 5: Post Matric Scholarship for SC (Category = SC, Income <= 250000)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(5, 'CATEGORY', 'EQ', 'SC'),
(5, 'INCOME', 'LTE', '250000');

-- Rules for Scheme 6: PM YASASVI OBC Scholarship (Category IN OBC,EBC,DNT, Income <= 250000)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(6, 'CATEGORY', 'IN', 'OBC,EBC,DNT'),
(6, 'INCOME', 'LTE', '250000');

-- Rules for Scheme 7: ST Scholarship (Category = ST, Income <= 250000)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(7, 'CATEGORY', 'EQ', 'ST'),
(7, 'INCOME', 'LTE', '250000');

-- Rules for Scheme 8: IGNOAPS Old Age Pension (Age >= 60, Income <= 100000 TODO: verify BPL income equivalent)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(8, 'AGE', 'GTE', '60'),
(8, 'INCOME', 'LTE', '100000');

-- Rules for Scheme 9: PMAY-U EWS (Age >= 18, Income <= 300000)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(9, 'AGE', 'GTE', '18'),
(9, 'INCOME', 'LTE', '300000');

-- Rules for Scheme 10: Ayushman Bharat 70+ Senior Citizen (Age >= 70)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(10, 'AGE', 'GTE', '70'),
(10, 'STATE', 'EQ', 'ALL');

-- Rules for Scheme 11: Majhi Ladki Bahin (State = Maharashtra, Age 21-65, Income <= 250000)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(11, 'STATE', 'EQ', 'Maharashtra'),
(11, 'AGE', 'GTE', '21'),
(11, 'AGE', 'LTE', '65'),
(11, 'INCOME', 'LTE', '250000');

-- Rules for Scheme 12: Kalaignar Magalir Urimai Thittam (State = Tamil Nadu, Age >= 21, Income <= 250000)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(12, 'STATE', 'EQ', 'Tamil Nadu'),
(12, 'AGE', 'GTE', '21'),
(12, 'INCOME', 'LTE', '250000');

-- Rules for Scheme 13: Yuva Nidhi (State = Karnataka, Age 21-35)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(13, 'STATE', 'EQ', 'Karnataka'),
(13, 'AGE', 'GTE', '21'),
(13, 'AGE', 'LTE', '35');

-- Rules for Scheme 14: PMMY Mudra Loan (Age >= 18, State = ALL)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(14, 'AGE', 'GTE', '18'),
(14, 'STATE', 'EQ', 'ALL');

-- Rules for Scheme 15: Babu Jagjivan Ram Chhatrawas (Category = SC, Age >= 16, Income <= 250000)
INSERT INTO scheme_rule (scheme_id, field, operator, rule_value) VALUES
(15, 'CATEGORY', 'EQ', 'SC'),
(15, 'AGE', 'GTE', '16'),
(15, 'INCOME', 'LTE', '250000');

-- Reset H2 auto-increment sequences past our seeded IDs so POST /schemes works seamlessly
ALTER TABLE scheme ALTER COLUMN id RESTART WITH 100;
ALTER TABLE scheme_rule ALTER COLUMN id RESTART WITH 100;
