interface UserInfo {
    username: string;
    email: string;
    phoneNumber: string;
    role: string;
}

interface ApiResponse {
    success: boolean;
    data?: UserInfo;
    error?: string;
}
