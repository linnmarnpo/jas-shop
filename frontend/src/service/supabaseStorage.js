/**
 * supabaseStorage.js
 * Uploads images via the Spring Boot backend — the Supabase service key
 * never leaves the server. The backend places files into:
 *   products/{product-name}/{uuid}.ext    e.g. products/red-shirt/abc.jpg
 *   categories/{category-name}/{uuid}.ext e.g. categories/shirts/abc.jpg
 */

import api from "./axios";

/**
 * Upload a file to Supabase Storage via the backend.
 * @param {File}   file   - The image file to upload
 * @param {'products' | 'categories'} folder - Target folder inside the bucket
 * @param {string} [name] - Optional product/category name used as a subfolder
 * @returns {Promise<string>} - The public URL of the uploaded image
 */
export async function uploadToSupabase(file, folder, name = "") {
  const formData = new FormData();
  formData.append("image", file);
  formData.append("folder", folder);
  if (name) formData.append("name", name);

  const response = await api.post("/images/upload", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  // Backend returns the plain public URL string
  return response.data;
}
