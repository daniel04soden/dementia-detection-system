export type User = {
  userID: string;
  role: string;
};

export async function getUser(): Promise<User | null> {
  try {
    const res = await fetch("/api/web/me", {
      method: "GET",
      credentials: "include", // sends HttpOnly cookies
    });

    if (!res.ok) {
      console.log("Failed to fetch /me");
      return null;
    }

    return await res.json();
  } catch (err) {
    console.error("Fetch error:", err);
    return null;
  }
}

export async function authorize(allowedRoles: string[]): Promise<boolean> {
  const user = await getUser();
  if (!user) return false;
  return allowedRoles.includes(user.role);
}
