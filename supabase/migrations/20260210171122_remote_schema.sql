


SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


COMMENT ON SCHEMA "public" IS 'standard public schema';



CREATE EXTENSION IF NOT EXISTS "pg_graphql" WITH SCHEMA "graphql";






CREATE EXTENSION IF NOT EXISTS "pg_stat_statements" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "pgcrypto" WITH SCHEMA "extensions";






CREATE EXTENSION IF NOT EXISTS "supabase_vault" WITH SCHEMA "vault";






CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA "extensions";





SET default_tablespace = '';

SET default_table_access_method = "heap";


CREATE TABLE IF NOT EXISTS "public"."collections" (
    "id" "uuid" DEFAULT "extensions"."uuid_generate_v4"() NOT NULL,
    "user_id" "uuid" NOT NULL,
    "name" "text" NOT NULL,
    "system_type" "text",
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "updated_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    CONSTRAINT "collections_system_type_check" CHECK (("system_type" = ANY (ARRAY['uncategorized'::"text", 'favorites'::"text"])))
);


ALTER TABLE "public"."collections" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."recipe_collections" (
    "recipe_id" "uuid" NOT NULL,
    "collection_id" "uuid" NOT NULL,
    "added_at" timestamp with time zone DEFAULT "now"() NOT NULL
);


ALTER TABLE "public"."recipe_collections" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."recipe_ingredients" (
    "id" "uuid" DEFAULT "extensions"."uuid_generate_v4"() NOT NULL,
    "recipe_id" "uuid" NOT NULL,
    "name" "text" NOT NULL,
    "quantity" double precision,
    "unit" "text",
    "preparation_notes" "text",
    "sort_order" integer NOT NULL
);


ALTER TABLE "public"."recipe_ingredients" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."recipe_instructions" (
    "id" "uuid" DEFAULT "extensions"."uuid_generate_v4"() NOT NULL,
    "recipe_id" "uuid" NOT NULL,
    "title" "text" NOT NULL,
    "instructions" "text" NOT NULL,
    "cook_duration_seconds" integer,
    "temperature_value" integer,
    "temperature_unit" "text",
    "sort_order" integer NOT NULL,
    CONSTRAINT "recipe_instructions_temperature_unit_check" CHECK (("temperature_unit" = ANY (ARRAY['C'::"text", 'F'::"text"])))
);


ALTER TABLE "public"."recipe_instructions" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."recipe_instructions_sections" (
    "id" "uuid" DEFAULT "extensions"."uuid_generate_v4"() NOT NULL,
    "recipe_id" "uuid" NOT NULL,
    "name" "text" NOT NULL,
    "sort_order" integer NOT NULL
);


ALTER TABLE "public"."recipe_instructions_sections" OWNER TO "postgres";


CREATE TABLE IF NOT EXISTS "public"."recipes" (
    "id" "uuid" DEFAULT "extensions"."uuid_generate_v4"() NOT NULL,
    "user_id" "uuid" NOT NULL,
    "title" "text" NOT NULL,
    "image_url" "text",
    "prep_time_seconds" integer NOT NULL,
    "cook_time_seconds" integer NOT NULL,
    "servings" integer DEFAULT 1 NOT NULL,
    "difficulty" integer,
    "created_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    "updated_at" timestamp with time zone DEFAULT "now"() NOT NULL,
    CONSTRAINT "recipes_difficulty_check" CHECK (("difficulty" = ANY (ARRAY[1, 2, 3])))
);


ALTER TABLE "public"."recipes" OWNER TO "postgres";


CREATE OR REPLACE VIEW "public"."recipes_with_details" WITH ("security_invoker"='on') AS
 SELECT "id",
    "user_id",
    "title",
    "image_url",
    "prep_time_seconds",
    "cook_time_seconds",
    "servings",
    "difficulty",
    "created_at",
    "updated_at",
    COALESCE(( SELECT "jsonb_agg"("jsonb_build_object"('id', "ri"."id", 'name', "ri"."name", 'quantity', "ri"."quantity", 'unit', "ri"."unit", 'preparation_notes', "ri"."preparation_notes", 'sort_order', "ri"."sort_order") ORDER BY "ri"."sort_order") AS "jsonb_agg"
           FROM "public"."recipe_ingredients" "ri"
          WHERE ("ri"."recipe_id" = "r"."id")), '[]'::"jsonb") AS "ingredients",
    COALESCE(( SELECT "jsonb_agg"("jsonb_build_object"('id', "ris"."id", 'name', "ris"."name", 'sort_order', "ris"."sort_order") ORDER BY "ris"."sort_order") AS "jsonb_agg"
           FROM "public"."recipe_instructions_sections" "ris"
          WHERE ("ris"."recipe_id" = "r"."id")), '[]'::"jsonb") AS "sections",
    COALESCE(( SELECT "jsonb_agg"("jsonb_build_object"('id', "ri"."id", 'title', "ri"."title", 'instructions', "ri"."instructions", 'cook_duration_seconds', "ri"."cook_duration_seconds", 'temperature_value', "ri"."temperature_value", 'temperature_unit', "ri"."temperature_unit", 'sort_order', "ri"."sort_order") ORDER BY "ri"."sort_order") AS "jsonb_agg"
           FROM "public"."recipe_instructions" "ri"
          WHERE ("ri"."recipe_id" = "r"."id")), '[]'::"jsonb") AS "instructions",
    COALESCE(( SELECT "jsonb_agg"("jsonb_build_object"('id', "c"."id", 'name', "c"."name", 'system_type', "c"."system_type")) AS "jsonb_agg"
           FROM ("public"."collections" "c"
             JOIN "public"."recipe_collections" "rc" ON (("rc"."collection_id" = "c"."id")))
          WHERE ("rc"."recipe_id" = "r"."id")), '[]'::"jsonb") AS "collections"
   FROM "public"."recipes" "r";


ALTER VIEW "public"."recipes_with_details" OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."get_recipe_with_details"("p_recipe_id" "uuid") RETURNS SETOF "public"."recipes_with_details"
    LANGUAGE "sql" SECURITY DEFINER
    SET "search_path" TO 'public', 'pg_temp'
    AS $$
    SELECT * FROM recipes_with_details
    WHERE id = p_recipe_id AND user_id = auth.uid();
$$;


ALTER FUNCTION "public"."get_recipe_with_details"("p_recipe_id" "uuid") OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."get_recipes_by_collection"("p_collection_id" "uuid") RETURNS SETOF "public"."recipes_with_details"
    LANGUAGE "sql" SECURITY DEFINER
    SET "search_path" TO 'public', 'pg_temp'
    AS $$
    SELECT rwd.* FROM recipes_with_details rwd
    INNER JOIN recipe_collections rc ON rc.recipe_id = rwd.id
    WHERE rc.collection_id = p_collection_id AND rwd.user_id = auth.uid();
$$;


ALTER FUNCTION "public"."get_recipes_by_collection"("p_collection_id" "uuid") OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."move_recipe_to_collections"("p_recipe_id" "uuid", "p_collection_ids" "uuid"[]) RETURNS boolean
    LANGUAGE "plpgsql" SECURITY DEFINER
    SET "search_path" TO 'public', 'pg_temp'
    AS $$
DECLARE
    v_collection_id UUID;
BEGIN
    -- Vérifier que la recette appartient à l'utilisateur
    IF NOT EXISTS (SELECT 1 FROM recipes WHERE id = p_recipe_id AND user_id = auth.uid()) THEN
        RETURN FALSE;
    END IF;

    -- Supprimer toutes les associations actuelles
    DELETE FROM recipe_collections WHERE recipe_id = p_recipe_id;

    -- Ajouter les nouvelles collections
    IF p_collection_ids IS NOT NULL AND array_length(p_collection_ids, 1) > 0 THEN
        FOREACH v_collection_id IN ARRAY p_collection_ids
        LOOP
            INSERT INTO recipe_collections (recipe_id, collection_id)
            VALUES (p_recipe_id, v_collection_id);
        END LOOP;
    END IF;

    RETURN TRUE;
END;
$$;


ALTER FUNCTION "public"."move_recipe_to_collections"("p_recipe_id" "uuid", "p_collection_ids" "uuid"[]) OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."update_recipe_with_details"("p_recipe_id" "uuid", "p_recipe" "jsonb", "p_collection_ids" "uuid"[] DEFAULT NULL::"uuid"[]) RETURNS boolean
    LANGUAGE "plpgsql" SECURITY DEFINER
    SET "search_path" TO 'public', 'pg_temp'
    AS $$
DECLARE
    v_ingredient JSONB;
    v_section JSONB;
    v_instruction JSONB;
    v_collection_id UUID;
BEGIN
    -- Vérifier que la recette appartient à l'utilisateur
    IF NOT EXISTS (SELECT 1 FROM recipes WHERE id = p_recipe_id AND user_id = auth.uid()) THEN
        RETURN FALSE;
    END IF;

    -- Mettre à jour la recette
    UPDATE recipes SET
        title = p_recipe->>'title',
        image_url = p_recipe->>'image_url',
        prep_time_seconds = (p_recipe->>'prep_time_seconds')::INTEGER,
        cook_time_seconds = (p_recipe->>'cook_time_seconds')::INTEGER,
        servings = (p_recipe->>'servings')::INTEGER,
        difficulty = (p_recipe->>'difficulty')::INTEGER
    WHERE id = p_recipe_id;

    -- Supprimer les anciennes données liées
    DELETE FROM recipe_ingredients WHERE recipe_id = p_recipe_id;
    DELETE FROM recipe_instructions_sections WHERE recipe_id = p_recipe_id;
    DELETE FROM recipe_instructions WHERE recipe_id = p_recipe_id;

    -- Insérer les nouveaux ingrédients
    FOR v_ingredient IN SELECT * FROM jsonb_array_elements(p_recipe->'ingredients')
    LOOP
        INSERT INTO recipe_ingredients (recipe_id, name, quantity, unit, preparation_notes, sort_order)
        VALUES (
            p_recipe_id,
            v_ingredient->>'name',
            (v_ingredient->>'quantity')::DOUBLE PRECISION,
            v_ingredient->>'unit',
            v_ingredient->>'preparation_notes',
            (v_ingredient->>'sort_order')::INTEGER
        );
    END LOOP;

    -- Insérer les nouvelles sections
    FOR v_section IN SELECT * FROM jsonb_array_elements(p_recipe->'sections')
    LOOP
        INSERT INTO recipe_instructions_sections (recipe_id, name, sort_order)
        VALUES (
            p_recipe_id,
            v_section->>'name',
            (v_section->>'sort_order')::INTEGER
        );
    END LOOP;

    -- Insérer les nouvelles instructions
    FOR v_instruction IN SELECT * FROM jsonb_array_elements(p_recipe->'instructions')
    LOOP
        INSERT INTO recipe_instructions (recipe_id, title, instructions, cook_duration_seconds, temperature_value, temperature_unit, sort_order)
        VALUES (
            p_recipe_id,
            v_instruction->>'title',
            v_instruction->>'instructions',
            (v_instruction->>'cook_duration_seconds')::INTEGER,
            (v_instruction->>'temperature_value')::INTEGER,
            v_instruction->>'temperature_unit',
            (v_instruction->>'sort_order')::INTEGER
        );
    END LOOP;

    -- Gérer les collections si fournies
    IF p_collection_ids IS NOT NULL THEN
        DELETE FROM recipe_collections WHERE recipe_id = p_recipe_id;
        
        FOREACH v_collection_id IN ARRAY p_collection_ids
        LOOP
            INSERT INTO recipe_collections (recipe_id, collection_id)
            VALUES (p_recipe_id, v_collection_id);
        END LOOP;
    END IF;

    RETURN TRUE;
END;
$$;


ALTER FUNCTION "public"."update_recipe_with_details"("p_recipe_id" "uuid", "p_recipe" "jsonb", "p_collection_ids" "uuid"[]) OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."update_updated_at_column"() RETURNS "trigger"
    LANGUAGE "plpgsql" SECURITY DEFINER
    SET "search_path" TO 'public', 'pg_temp'
    AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;


ALTER FUNCTION "public"."update_updated_at_column"() OWNER TO "postgres";


ALTER TABLE ONLY "public"."collections"
    ADD CONSTRAINT "collections_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."collections"
    ADD CONSTRAINT "collections_user_id_system_type_key" UNIQUE ("user_id", "system_type");



ALTER TABLE ONLY "public"."recipe_collections"
    ADD CONSTRAINT "recipe_collections_pkey" PRIMARY KEY ("recipe_id", "collection_id");



ALTER TABLE ONLY "public"."recipe_ingredients"
    ADD CONSTRAINT "recipe_ingredients_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."recipe_instructions"
    ADD CONSTRAINT "recipe_instructions_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."recipe_instructions_sections"
    ADD CONSTRAINT "recipe_instructions_sections_pkey" PRIMARY KEY ("id");



ALTER TABLE ONLY "public"."recipes"
    ADD CONSTRAINT "recipes_pkey" PRIMARY KEY ("id");



CREATE INDEX "idx_collections_user_id" ON "public"."collections" USING "btree" ("user_id");



CREATE INDEX "idx_recipe_collections_collection_id" ON "public"."recipe_collections" USING "btree" ("collection_id");



CREATE INDEX "idx_recipe_collections_recipe_id" ON "public"."recipe_collections" USING "btree" ("recipe_id");



CREATE INDEX "idx_recipe_ingredients_recipe_id" ON "public"."recipe_ingredients" USING "btree" ("recipe_id");



CREATE INDEX "idx_recipe_instructions_recipe_id" ON "public"."recipe_instructions" USING "btree" ("recipe_id");



CREATE INDEX "idx_recipe_instructions_sections_recipe_id" ON "public"."recipe_instructions_sections" USING "btree" ("recipe_id");



CREATE INDEX "idx_recipes_user_id" ON "public"."recipes" USING "btree" ("user_id");



CREATE OR REPLACE TRIGGER "update_collections_updated_at" BEFORE UPDATE ON "public"."collections" FOR EACH ROW EXECUTE FUNCTION "public"."update_updated_at_column"();



CREATE OR REPLACE TRIGGER "update_recipes_updated_at" BEFORE UPDATE ON "public"."recipes" FOR EACH ROW EXECUTE FUNCTION "public"."update_updated_at_column"();



ALTER TABLE ONLY "public"."collections"
    ADD CONSTRAINT "collections_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."recipe_collections"
    ADD CONSTRAINT "recipe_collections_collection_id_fkey" FOREIGN KEY ("collection_id") REFERENCES "public"."collections"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."recipe_collections"
    ADD CONSTRAINT "recipe_collections_recipe_id_fkey" FOREIGN KEY ("recipe_id") REFERENCES "public"."recipes"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."recipe_ingredients"
    ADD CONSTRAINT "recipe_ingredients_recipe_id_fkey" FOREIGN KEY ("recipe_id") REFERENCES "public"."recipes"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."recipe_instructions"
    ADD CONSTRAINT "recipe_instructions_recipe_id_fkey" FOREIGN KEY ("recipe_id") REFERENCES "public"."recipes"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."recipe_instructions_sections"
    ADD CONSTRAINT "recipe_instructions_sections_recipe_id_fkey" FOREIGN KEY ("recipe_id") REFERENCES "public"."recipes"("id") ON DELETE CASCADE;



ALTER TABLE ONLY "public"."recipes"
    ADD CONSTRAINT "recipes_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "auth"."users"("id") ON DELETE CASCADE;



CREATE POLICY "Users can delete from their recipe collections" ON "public"."recipe_collections" FOR DELETE USING ((EXISTS ( SELECT 1
   FROM "public"."collections"
  WHERE (("collections"."id" = "recipe_collections"."collection_id") AND ("collections"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can delete ingredients of their recipes" ON "public"."recipe_ingredients" FOR DELETE USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_ingredients"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can delete instructions of their recipes" ON "public"."recipe_instructions" FOR DELETE USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_instructions"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can delete sections of their recipes" ON "public"."recipe_instructions_sections" FOR DELETE USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_instructions_sections"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can delete their own collections" ON "public"."collections" FOR DELETE USING (("auth"."uid"() = "user_id"));



CREATE POLICY "Users can delete their own recipes" ON "public"."recipes" FOR DELETE USING (("auth"."uid"() = "user_id"));



CREATE POLICY "Users can insert ingredients to their recipes" ON "public"."recipe_ingredients" FOR INSERT WITH CHECK ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_ingredients"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can insert instructions to their recipes" ON "public"."recipe_instructions" FOR INSERT WITH CHECK ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_instructions"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can insert sections to their recipes" ON "public"."recipe_instructions_sections" FOR INSERT WITH CHECK ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_instructions_sections"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can insert their own collections" ON "public"."collections" FOR INSERT WITH CHECK (("auth"."uid"() = "user_id"));



CREATE POLICY "Users can insert their own recipes" ON "public"."recipes" FOR INSERT WITH CHECK (("auth"."uid"() = "user_id"));



CREATE POLICY "Users can insert to their recipe collections" ON "public"."recipe_collections" FOR INSERT WITH CHECK (((EXISTS ( SELECT 1
   FROM "public"."collections"
  WHERE (("collections"."id" = "recipe_collections"."collection_id") AND ("collections"."user_id" = "auth"."uid"())))) AND (EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_collections"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"()))))));



CREATE POLICY "Users can update ingredients of their recipes" ON "public"."recipe_ingredients" FOR UPDATE USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_ingredients"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can update instructions of their recipes" ON "public"."recipe_instructions" FOR UPDATE USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_instructions"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can update sections of their recipes" ON "public"."recipe_instructions_sections" FOR UPDATE USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_instructions_sections"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can update their own collections" ON "public"."collections" FOR UPDATE USING (("auth"."uid"() = "user_id")) WITH CHECK (("auth"."uid"() = "user_id"));



CREATE POLICY "Users can update their own recipes" ON "public"."recipes" FOR UPDATE USING (("auth"."uid"() = "user_id")) WITH CHECK (("auth"."uid"() = "user_id"));



CREATE POLICY "Users can update their recipe collections" ON "public"."recipe_collections" FOR UPDATE USING (((EXISTS ( SELECT 1
   FROM "public"."collections"
  WHERE (("collections"."id" = "recipe_collections"."collection_id") AND ("collections"."user_id" = "auth"."uid"())))) AND (EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_collections"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))))) WITH CHECK (((EXISTS ( SELECT 1
   FROM "public"."collections"
  WHERE (("collections"."id" = "recipe_collections"."collection_id") AND ("collections"."user_id" = "auth"."uid"())))) AND (EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_collections"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"()))))));



CREATE POLICY "Users can view ingredients of their recipes" ON "public"."recipe_ingredients" FOR SELECT USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_ingredients"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can view instructions of their recipes" ON "public"."recipe_instructions" FOR SELECT USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_instructions"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can view sections of their recipes" ON "public"."recipe_instructions_sections" FOR SELECT USING ((EXISTS ( SELECT 1
   FROM "public"."recipes"
  WHERE (("recipes"."id" = "recipe_instructions_sections"."recipe_id") AND ("recipes"."user_id" = "auth"."uid"())))));



CREATE POLICY "Users can view their own collections" ON "public"."collections" FOR SELECT USING (("auth"."uid"() = "user_id"));



CREATE POLICY "Users can view their own recipes" ON "public"."recipes" FOR SELECT USING (("auth"."uid"() = "user_id"));



CREATE POLICY "Users can view their recipe collections" ON "public"."recipe_collections" FOR SELECT USING ((EXISTS ( SELECT 1
   FROM "public"."collections"
  WHERE (("collections"."id" = "recipe_collections"."collection_id") AND ("collections"."user_id" = "auth"."uid"())))));



ALTER TABLE "public"."collections" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."recipe_collections" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."recipe_ingredients" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."recipe_instructions" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."recipe_instructions_sections" ENABLE ROW LEVEL SECURITY;


ALTER TABLE "public"."recipes" ENABLE ROW LEVEL SECURITY;




ALTER PUBLICATION "supabase_realtime" OWNER TO "postgres";


GRANT USAGE ON SCHEMA "public" TO "postgres";
GRANT USAGE ON SCHEMA "public" TO "anon";
GRANT USAGE ON SCHEMA "public" TO "authenticated";
GRANT USAGE ON SCHEMA "public" TO "service_role";

























































































































































GRANT ALL ON TABLE "public"."collections" TO "anon";
GRANT ALL ON TABLE "public"."collections" TO "authenticated";
GRANT ALL ON TABLE "public"."collections" TO "service_role";



GRANT ALL ON TABLE "public"."recipe_collections" TO "anon";
GRANT ALL ON TABLE "public"."recipe_collections" TO "authenticated";
GRANT ALL ON TABLE "public"."recipe_collections" TO "service_role";



GRANT ALL ON TABLE "public"."recipe_ingredients" TO "anon";
GRANT ALL ON TABLE "public"."recipe_ingredients" TO "authenticated";
GRANT ALL ON TABLE "public"."recipe_ingredients" TO "service_role";



GRANT ALL ON TABLE "public"."recipe_instructions" TO "anon";
GRANT ALL ON TABLE "public"."recipe_instructions" TO "authenticated";
GRANT ALL ON TABLE "public"."recipe_instructions" TO "service_role";



GRANT ALL ON TABLE "public"."recipe_instructions_sections" TO "anon";
GRANT ALL ON TABLE "public"."recipe_instructions_sections" TO "authenticated";
GRANT ALL ON TABLE "public"."recipe_instructions_sections" TO "service_role";



GRANT ALL ON TABLE "public"."recipes" TO "anon";
GRANT ALL ON TABLE "public"."recipes" TO "authenticated";
GRANT ALL ON TABLE "public"."recipes" TO "service_role";



GRANT ALL ON TABLE "public"."recipes_with_details" TO "anon";
GRANT ALL ON TABLE "public"."recipes_with_details" TO "authenticated";
GRANT ALL ON TABLE "public"."recipes_with_details" TO "service_role";



GRANT ALL ON FUNCTION "public"."get_recipe_with_details"("p_recipe_id" "uuid") TO "anon";
GRANT ALL ON FUNCTION "public"."get_recipe_with_details"("p_recipe_id" "uuid") TO "authenticated";
GRANT ALL ON FUNCTION "public"."get_recipe_with_details"("p_recipe_id" "uuid") TO "service_role";



GRANT ALL ON FUNCTION "public"."get_recipes_by_collection"("p_collection_id" "uuid") TO "anon";
GRANT ALL ON FUNCTION "public"."get_recipes_by_collection"("p_collection_id" "uuid") TO "authenticated";
GRANT ALL ON FUNCTION "public"."get_recipes_by_collection"("p_collection_id" "uuid") TO "service_role";



GRANT ALL ON FUNCTION "public"."move_recipe_to_collections"("p_recipe_id" "uuid", "p_collection_ids" "uuid"[]) TO "anon";
GRANT ALL ON FUNCTION "public"."move_recipe_to_collections"("p_recipe_id" "uuid", "p_collection_ids" "uuid"[]) TO "authenticated";
GRANT ALL ON FUNCTION "public"."move_recipe_to_collections"("p_recipe_id" "uuid", "p_collection_ids" "uuid"[]) TO "service_role";



GRANT ALL ON FUNCTION "public"."update_recipe_with_details"("p_recipe_id" "uuid", "p_recipe" "jsonb", "p_collection_ids" "uuid"[]) TO "anon";
GRANT ALL ON FUNCTION "public"."update_recipe_with_details"("p_recipe_id" "uuid", "p_recipe" "jsonb", "p_collection_ids" "uuid"[]) TO "authenticated";
GRANT ALL ON FUNCTION "public"."update_recipe_with_details"("p_recipe_id" "uuid", "p_recipe" "jsonb", "p_collection_ids" "uuid"[]) TO "service_role";



GRANT ALL ON FUNCTION "public"."update_updated_at_column"() TO "anon";
GRANT ALL ON FUNCTION "public"."update_updated_at_column"() TO "authenticated";
GRANT ALL ON FUNCTION "public"."update_updated_at_column"() TO "service_role";
























ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON SEQUENCES TO "service_role";






ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON FUNCTIONS TO "service_role";






ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "postgres";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "anon";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "authenticated";
ALTER DEFAULT PRIVILEGES FOR ROLE "postgres" IN SCHEMA "public" GRANT ALL ON TABLES TO "service_role";































drop extension if exists "pg_net";


