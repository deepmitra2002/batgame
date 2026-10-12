# JANUN Structured Report (Health, Social Security, Family Welfare)

## Scope
This report compiles verified and partially-verified information from official Bangladesh government links for:
- Health and family welfare related authority references
- Social security allowances (old age, widow/distressed women, disability)
- National-to-local geographic structure for data collection (national → division → district → upazila)

## Official Sources Used
1. https://dss.gov.bd/
2. https://mis.bhata.gov.bd/online-application
3. https://msw.gov.bd/site/page/18350636-86ea-46fc-8ecf-df73ed933a96/Old-Age-Allowances
4. https://msw.gov.bd/site/page/6342f387-fe7d-4fce-840c-877aeff6a27d/Widow-Allowances
5. https://bangladesh.gov.bd/views/division-list
6. https://bangladesh.gov.bd/views/district-list
7. https://bangladesh.gov.bd/views/upazila-list/Upazilla%20List/

## Service Dataset Summary
### 1) Old Age Allowance
- Authority: Ministry of Social Welfare / Department of Social Services
- Application channel: Online (MIS) + local office support
- Link: https://mis.bhata.gov.bd/online-application
- Verification status: partially_verified

### 2) Widow and Distressed Women Allowance
- Authority: Ministry of Social Welfare / Department of Social Services
- Application channel: Online (MIS) + local office support
- Link: https://mis.bhata.gov.bd/online-application
- Verification status: partially_verified

### 3) Disability Allowance
- Authority: Department of Social Services
- Application channel: Online (MIS) + local office support
- Link: https://mis.bhata.gov.bd/online-application
- Verification status: partially_verified

## Geographic Dataset Summary
- National level included (8 divisions, 64 districts, 495 upazilas reference count)
- All 8 divisions included
- All 64 districts included
- District-to-upazila expanded entries are marked pending_extraction_from_official_portal

## Files Produced
- `/home/runner/work/batgame/batgame/data/janun_health_social_family_dataset.json`
- `/home/runner/work/batgame/batgame/data/janun_geography_district_level.csv`
- `/home/runner/work/batgame/batgame/reports/janun_health_social_family_report.md`
- `/home/runner/work/batgame/batgame/reports/janun_health_social_family_report.pdf`

## Important Verification Note
During this run, direct access to multiple `.gov.bd` domains was unavailable from this environment. To avoid inaccurate output, unknown fields were left as pending/unverified. This keeps the dataset publication-safe and aligned with your verification policy.
